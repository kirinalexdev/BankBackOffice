package com.kirinalex.BankBackOffice.kafka;

import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.services.CardOrderService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaConsumer {

    private final CardOrderService cardOrderService;

    @KafkaListener(topics = "backoffice.cardorder", groupId = "backoffice")
    public void consume(CardOrder cardOrder) {
        cardOrderService.save(cardOrder);
        System.out.println("consumer get: " + cardOrder);
    }
}
