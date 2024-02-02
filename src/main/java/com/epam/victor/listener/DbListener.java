package com.epam.victor.listener;

import com.epam.victor.model.ProcessedMessage;
import com.epam.victor.model.Status;
import com.epam.victor.service.ProcessedMessageService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbListener {

    private final ProcessedMessageService processedMessageService;
    private final RabbitTemplate rabbitTemplate;


    @Autowired
    public DbListener(ProcessedMessageService processedMessageService, RabbitTemplate rabbitTemplate) {
        this.processedMessageService = processedMessageService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(cron = "0 * * ? * *")
    public void resubmitTask (){
        List<ProcessedMessage> messages = processedMessageService.getAllByStatus(Status.RETURNED);
        for (ProcessedMessage message : messages){
            rabbitTemplate.convertAndSend("notification_exchange",
                    message.getDelivery() ? "notification1" : "notification2",
                    message.getMessage().replace("retry",""));
            message.setStatus(Status.DISCARDED);
            processedMessageService.update(message);
        }









    }

}
