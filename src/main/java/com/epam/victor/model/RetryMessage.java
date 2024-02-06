package com.epam.victor.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "retry_message")
public class RetryMessage{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Instant lastRetryTime;

    @Enumerated(EnumType.STRING)
    private RetryStatus retryStatus;

    private Float failRate;

    private Integer retryCount;

    @OneToOne
    @JoinColumn(name = "message_uuid", referencedColumnName = "uuid")
    private ProcessedMessage message;

    public RetryMessage(Instant lastRetryTime, RetryStatus retryStatus, Float failRate, Integer retryCount, ProcessedMessage message) {
        this.lastRetryTime = lastRetryTime;
        this.retryStatus = retryStatus;
        this.failRate = failRate;
        this.retryCount = retryCount;
        this.message = message;
    }
}
