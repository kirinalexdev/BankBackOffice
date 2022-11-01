package com.kirinalex.BankBackOffice.services;

import com.kirinalex.BankBackOffice.kafka.KafkaProducer;
import com.kirinalex.BankBackOffice.models.CardOrder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CardOrderService {

    private KafkaProducer kafkaProducer;

    // TODO возвращать неуспех если сервис не выдал результат?
    public void create(CardOrder cardOrder){

        kafkaProducer.sendMessage(cardOrder);
    }

}
