package com.kirinalex.BankBackOffice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    // TODO добавить параметров кроме name, показать масштабируемость: partitions и т.п.
    @Bean
    public NewTopic СardOrder(){
        return TopicBuilder.name("backoffice.cardorder")
                .build();
    }
}
