package com.epam.victor.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class ProcessedMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String message;

    Boolean delivery;

    @Enumerated(EnumType.STRING)
    Status status;

    public ProcessedMessage(String message, Status status, boolean delivery) {
        this.message = message;
        this.status = status;
        this.delivery = delivery;
    }
}
