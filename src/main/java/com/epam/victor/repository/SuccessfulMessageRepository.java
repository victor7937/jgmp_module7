package com.epam.victor.repository;

import com.epam.victor.model.SuccessfulMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuccessfulMessageRepository extends JpaRepository<SuccessfulMessage, Integer> {
}
