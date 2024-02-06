package com.epam.victor.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "successful_message")
public class SuccessfulMessage{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Instant receivedTime;

    @OneToOne
    @JoinColumn(name = "message_uuid", referencedColumnName = "uuid")
    private ProcessedMessage message;

    public SuccessfulMessage(Instant receivedTime, ProcessedMessage message) {
        this.receivedTime = receivedTime;
        this.message = message;
    }
}
