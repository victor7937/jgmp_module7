package com.epam.victor.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "failed_message")
public class FailedMessage{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String errorDescription;

    private Instant discardTime;

    @OneToOne
    @JoinColumn(name = "message_uuid", referencedColumnName = "uuid")
    private ProcessedMessage message;


    public FailedMessage(String errorDescription, Instant discardTime, ProcessedMessage message) {
        this.errorDescription = errorDescription;
        this.discardTime = discardTime;
        this.message = message;
    }
}
