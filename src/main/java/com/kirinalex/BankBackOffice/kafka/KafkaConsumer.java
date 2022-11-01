package com.kirinalex.BankBackOffice.kafka;

import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.services.CardOrderService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaConsumer {
// TODO реализовать Consumer отдельным проектом или этот проект сделать мультимодульным?

    private final CardOrderService cardOrderService;

    @KafkaListener(topics = "backoffice.cardorder", groupId = "group1") // TODO хардкодом так backoffice.cardorder и group1 или где хранить?
    public void consume(CardOrder cardOrder) {
        cardOrderService.save(cardOrder);
        System.out.println("consumer get: " + cardOrder);
    }
}
