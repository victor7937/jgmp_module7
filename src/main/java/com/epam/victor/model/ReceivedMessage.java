package com.epam.victor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceivedMessage {
    private String uuid;
    private String text;
    private Integer type;
    private Float failRate;
    private Instant sendTime;
}
