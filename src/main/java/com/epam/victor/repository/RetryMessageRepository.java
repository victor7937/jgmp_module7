package com.epam.victor.repository;

import com.epam.victor.model.RetryMessage;
import com.epam.victor.model.RetryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RetryMessageRepository extends JpaRepository<RetryMessage, Integer> {
    List<RetryMessage> findByRetryStatus(RetryStatus retryStatus);

    Optional<RetryMessage> findByMessage_Uuid(String uuid);

}
