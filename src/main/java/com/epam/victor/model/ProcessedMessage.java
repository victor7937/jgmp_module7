package com.epam.victor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class ProcessedMessage {

    @Id
    @Column(name = "uuid")
    private String uuid;

    private String text;

    private Integer type;

    private Instant sendTime;

}
