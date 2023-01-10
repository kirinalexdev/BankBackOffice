package com.kirinalex.BankBackOffice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ObjectMapperConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void setUp() {
        objectMapper
            .registerModule(new JavaTimeModule()) // позволяет избежать ошибки Type definition error: [simple type, class java.time.LocalDateTime];
                                                  // совместно с модулем jackson-datatype-jsr310
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // позволяет сериализовывать LocalDateTime в нормальный формат даты,
                                                                                     // а не в массив. проблема гуглится по "LocalDateTime as array"
    }

}
