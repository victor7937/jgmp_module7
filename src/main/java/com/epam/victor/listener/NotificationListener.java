package com.epam.victor.listener;

import com.epam.victor.model.ReceivedMessage;
import com.epam.victor.service.ProcessedMessageService;
import com.epam.victor.util.converter.JsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.epam.victor.model.QueueConstants.*;

// todo Разделить сообщения в базе
// todo Сделать Retry вероятность
// todo Retry очередь
@Component
@Slf4j
public class NotificationListener {
    public static final String HEADER_X_RETRIES_COUNT = "x-retries-count";
    public static final int MAX_RETRIES = 3;

    private final ProcessedMessageService processedMessageService;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public NotificationListener(ProcessedMessageService processedMessageService, RabbitTemplate rabbitTemplate) {
        this.processedMessageService = processedMessageService;
        this.rabbitTemplate = rabbitTemplate;
    }


    @RabbitListener(queues = NOTIFICATION_QUEUE1)
    public void processQueue1(String message) {
        ReceivedMessage messageObject = JsonConverter.fromJson(message);
        log.info("From notification1: {}", messageObject);
        processedMessageService.saveNew(messageObject);
    }

    @RabbitListener(queues = NOTIFICATION_QUEUE2)
    public void processQueue2(String message) {
        ReceivedMessage messageObject = JsonConverter.fromJson(message);
        log.info("From notification2: {}", messageObject);
        processedMessageService.saveNew(messageObject);
    }

    @RabbitListener(queues = NOTIFICATIONS_DLQ)
    public void processFailedMessages(Message message) {
        ReceivedMessage messageObject = JsonConverter.fromJson(new String(message.getBody()));
        log.info("Received failed message: {}", messageObject.getUuid());
        Integer retryCount = message.getMessageProperties().getHeader(HEADER_X_RETRIES_COUNT);
        processedMessageService.saveFailed(messageObject, retryCount);

    }

    @RabbitListener(queues = NOTIFICATION_QUEUE_RETRY)
    public void processRetryMessages(String message) {
        ReceivedMessage messageObject = JsonConverter.fromJson(message);
        log.info("Received retry message: {}", messageObject);
        processedMessageService.saveRetried(messageObject);
    }



}
