package com.epam.victor.util.converter;

import com.epam.victor.model.ReceivedMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.epam.victor.exception.ConsumerJsonException;

public class JsonConverter {

    public final static ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.findAndRegisterModules();
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static ReceivedMessage fromJson (String json) {
        ReceivedMessage messageObject;
        try {
            messageObject = OBJECT_MAPPER.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new ConsumerJsonException(e);
        }
        return messageObject;
    }

    public static String toJson(ReceivedMessage receivedMessage){
        try {
            return OBJECT_MAPPER.writeValueAsString(receivedMessage);
        } catch (JsonProcessingException e) {
            throw new ConsumerJsonException(e);
        }
    }

}


