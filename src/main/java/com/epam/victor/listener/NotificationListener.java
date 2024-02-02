package com.epam.victor.listener;

import com.epam.victor.service.ProcessedMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// todo Разделить сообщения в базе
// todo Сделать Retry вероятность
// todo Retry очередь
@Component
@Slf4j
public class NotificationListener {

    private final ProcessedMessageService processedMessageService;


    @Autowired
    public NotificationListener(ProcessedMessageService processedMessageService) {
        this.processedMessageService = processedMessageService;
    }


    @RabbitListener(queues = "notification_queue1")
    public void processQueue1(String message) {
        log.info("From notification1: {}", message);
        processedMessageService.chooseStatusAndSave(message, true);
    }

    @RabbitListener(queues = "notification_queue2")
    public void processQueue2(String message) {
        log.info("From notification2: {}", message);
        processedMessageService.chooseStatusAndSave(message, false);
    }

    @RabbitListener(queues = "notifications_dlq")
    public void processFailedMessages(String message) {
        log.info("Received failed message: {}", message);
        processedMessageService.saveDiscarded(message, false);
    }


}
