package com.example.demo;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author yeqiang
 * @since 5/22/20 10:00 AM
 */
public class LocalDateTimeTest {

    @Test
    public void jacksonTest() {
        LocalDateTime dt = LocalDateTime.now();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(dt);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
