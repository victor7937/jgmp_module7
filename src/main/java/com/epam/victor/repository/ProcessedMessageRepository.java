package com.epam.victor.repository;

import com.epam.victor.model.ProcessedMessage;
import com.epam.victor.model.RetryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessedMessageRepository extends JpaRepository<ProcessedMessage, String> {

}
