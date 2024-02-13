package com.epam.victor.listener;

import com.epam.victor.model.ProcessedMessage;
import com.epam.victor.model.ReceivedMessage;
import com.epam.victor.model.RetryMessage;
import com.epam.victor.model.RetryStatus;
import com.epam.victor.service.ProcessedMessageService;
import com.epam.victor.util.converter.JsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.victor.listener.NotificationListener.HEADER_X_RETRIES_COUNT;
import static com.epam.victor.model.QueueConstants.*;

@Component
@Slf4j
public class DbListener {

    private final ProcessedMessageService processedMessageService;
    private final RabbitTemplate rabbitTemplate;


    @Autowired
    public DbListener(ProcessedMessageService processedMessageService, RabbitTemplate rabbitTemplate) {
        this.processedMessageService = processedMessageService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void resubmitTask (){
        List<RetryMessage> messages = processedMessageService.getAllByStatus(RetryStatus.RETRY_IN_PROGRESS);
        log.info("Resubmit job started. Found {} messages to retry", messages.size());
        for (RetryMessage message : messages){
            Integer retryCount = message.getRetryCount() + 1;
            rabbitTemplate.convertAndSend(
                    NOTIFICATION_EXCHANGE,
                    ROUTING_KEY_RETRY,
                    JsonConverter.toJson(convertToReceived(message)),
                    m -> {
                        m.getMessageProperties().getHeaders().put(HEADER_X_RETRIES_COUNT, retryCount);
                        return m;
                    });
        }
    }

    private ReceivedMessage convertToReceived (RetryMessage retryMessage){
        ProcessedMessage processedMessage = retryMessage.getMessage();
        return new ReceivedMessage(
                processedMessage.getUuid(),
                processedMessage.getText(),
                processedMessage.getType(),
                retryMessage.getFailRate(),
                processedMessage.getSendTime());
    }

}
