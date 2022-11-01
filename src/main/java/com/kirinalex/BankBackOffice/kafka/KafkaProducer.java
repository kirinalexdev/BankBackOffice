package com.kirinalex.BankBackOffice.kafka;

import com.kirinalex.BankBackOffice.models.CardOrder;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducer {

    private KafkaTemplate<String, CardOrder> kafkaTemplate;

    public void sendMessage(CardOrder cardOrder){

        Message<CardOrder> message = MessageBuilder
                .withPayload(cardOrder)
                .setHeader(KafkaHeaders.TOPIC, "backoffice.cardorder") // TODO переименовать
                .build();

        kafkaTemplate.send(message);
    }
}
