package com.kirinalex.BankBackOffice.kafka;

import com.kirinalex.BankBackOffice.models.CardOrder;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaConsumer {
// TODO реализовать Consumer отдельным проектом или этот проект сделать мультимодульным?

    @KafkaListener(topics = "topic1", groupId = "group1") // TODO хардкодом так topic1 и group1 или где хранить?
    public void consume(CardOrder cardOrder) {
        System.out.println("consumer get: " + cardOrder);
    }
}
