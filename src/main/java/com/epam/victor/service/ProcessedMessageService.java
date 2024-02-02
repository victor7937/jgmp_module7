package com.epam.victor.service;

import com.epam.victor.exception.IncorrectMessageException;
import com.epam.victor.model.ProcessedMessage;
import com.epam.victor.model.Status;
import com.epam.victor.repository.ProcessedMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessedMessageService {

    private final ProcessedMessageRepository processedMessageRepository;

    @Autowired
    public ProcessedMessageService(ProcessedMessageRepository processedMessageRepository) {
        this.processedMessageRepository = processedMessageRepository;
    }

    public void chooseStatusAndSave(String message, boolean delivery){
        if (message.contains("retry")){
            processedMessageRepository.save(new ProcessedMessage(message, Status.RETURNED, delivery));
        } else if (message.contains("error")) {
            throw new IncorrectMessageException("Message " + message +" has an error");
        } else {
            processedMessageRepository.save(new ProcessedMessage(message, Status.ARCHIVED, delivery));
        }
    }

    public void saveDiscarded(String message, boolean delivery){
        processedMessageRepository.save(new ProcessedMessage(message, Status.DISCARDED, delivery));
    }

    public List<ProcessedMessage> getAllByStatus(Status status){
        return processedMessageRepository.findAllByStatus(status);
    }

    public void update (ProcessedMessage processedMessage){
        processedMessageRepository.save(processedMessage);
    }
}
