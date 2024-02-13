package com.epam.victor.service;

import com.epam.victor.exception.IdNotFoundException;
import com.epam.victor.exception.IncorrectMessageException;
import com.epam.victor.model.*;
import com.epam.victor.repository.FailedMessageRepository;
import com.epam.victor.repository.ProcessedMessageRepository;
import com.epam.victor.repository.RetryMessageRepository;
import com.epam.victor.repository.SuccessfulMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class ProcessedMessageService {

    private final ProcessedMessageRepository processedMessageRepository;
    private final SuccessfulMessageRepository successfulMessageRepository;

    private final FailedMessageRepository failedMessageRepository;

    private final RetryMessageRepository retryMessageRepository;

    @Value("${notification.app.max.retry.count}")
    private Integer maxRetryCount;

    @Autowired
    public ProcessedMessageService(ProcessedMessageRepository processedMessageRepository,
                                   SuccessfulMessageRepository successfulMessageRepository,
                                   FailedMessageRepository failedMessageRepository,
                                   RetryMessageRepository retryMessageRepository) {
        this.processedMessageRepository = processedMessageRepository;
        this.successfulMessageRepository = successfulMessageRepository;
        this.failedMessageRepository = failedMessageRepository;
        this.retryMessageRepository = retryMessageRepository;
    }

    public void saveNew(ReceivedMessage message){
        String uuid = message.getUuid();
        ProcessedMessage processedMessage = new ProcessedMessage(
                message.getUuid(),
                message.getText(),
                message.getType(),
                message.getSendTime());
        processedMessageRepository.save(processedMessage);
        if (isFail(message.getFailRate())){
            log.error("message {} was processed with error", uuid);
            throw new IncorrectMessageException("Message " + uuid +" has an error");
        } else {
            SuccessfulMessage successfulMessage = new SuccessfulMessage(Instant.now(), processedMessage);
            successfulMessageRepository.save(successfulMessage);
            log.info("message {} was processed successfully", uuid);
        }
    }

    public void saveRetried(ReceivedMessage message){
        String uuid = message.getUuid();
        if (isFail(message.getFailRate())){
            log.error("Retry message {} was processed with error", uuid);
            throw new IncorrectMessageException("Retry message " + message.getUuid() +" has an error");
        } else {
            RetryMessage retryMessage = findRetryByUuid(uuid);
            SuccessfulMessage successfulMessage = new SuccessfulMessage(Instant.now(), retryMessage.getMessage());
            successfulMessageRepository.save(successfulMessage);
            retryMessage.setRetryStatus(RetryStatus.RETRY_SUCCESSFUL);
            retryMessageRepository.save(retryMessage);
            log.info("Retry message {} was processed successfully with attempts {}", uuid, retryMessage.getRetryCount());
        }
    }

    public void saveFailed(ReceivedMessage message, Integer retryCount) {
        String uuid = message.getUuid();
        if (retryCount == null){
            retryCount = 1;
        }
        if (retryCount > maxRetryCount) {
            log.info("Message {} was discarded", uuid);
            saveAsDiscarded(uuid, retryCount);
            return;
        }
        log.info("Retrying message {} for the {} time", uuid, retryCount);
        saveAsNextRetry(uuid, retryCount, message.getFailRate());
    }


    public List<RetryMessage> getAllByStatus(RetryStatus status){
        return retryMessageRepository.findByRetryStatus(status);
    }

    public ProcessedMessage findOriginalByUuid(String uuid){
        return processedMessageRepository.findById(uuid).orElseThrow(
                () -> new IdNotFoundException("Id " + uuid + " was not found"));
    }

    public RetryMessage findRetryByUuid (String uuid){
        return retryMessageRepository.findByMessage_Uuid(uuid).orElseThrow(
                () -> new IdNotFoundException("Id " + uuid + " was not found"));
    }

    private void saveAsDiscarded(String uuid, int retryCount){
        FailedMessage failedMessage = new FailedMessage(
                "retry count exceeded with " + (retryCount - 1) + " times",
                Instant.now(),
                findOriginalByUuid(uuid));
        failedMessageRepository.save(failedMessage);
        if (maxRetryCount != 0){
            RetryMessage retryMessage = findRetryByUuid(uuid);
            retryMessage.setRetryStatus(RetryStatus.RETRY_FAILED);
            retryMessageRepository.save(retryMessage);
        }
    }

    private void saveAsNextRetry(String uuid, int retryCount, float failRate){
        RetryMessage retryMessage;
        if (retryCount == 1){
            retryMessage = new RetryMessage(
                    Instant.now(),
                    RetryStatus.RETRY_IN_PROGRESS,
                    failRate,
                    retryCount,
                    findOriginalByUuid(uuid));
        } else {
            retryMessage = findRetryByUuid(uuid);
            retryMessage.setRetryCount(retryCount);
            retryMessage.setLastRetryTime(Instant.now());
        }
        retryMessageRepository.save(retryMessage);
    }

    private boolean isFail(float failRate){
        int percentageRate = (int) (failRate * 100);
        int randNum = new Random().nextInt(100);
        return randNum < percentageRate;
    }
}
