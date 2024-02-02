package com.epam.victor.repository;

import com.epam.victor.model.ProcessedMessage;
import com.epam.victor.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessedMessageRepository extends JpaRepository<ProcessedMessage, Integer> {
    List<ProcessedMessage> findAllByStatus (Status status);

}
