package com.kirinalex.BankBackOffice.services;

import com.kirinalex.BankBackOffice.kafka.KafkaProducer;
import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.repositories.CardOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CardOrderService {

    private KafkaProducer kafkaProducer;

    private final CardOrderRepository cardOrderRepository;

    @Transactional
    public void save(CardOrder cardOrder) {
        cardOrderRepository.save(cardOrder);
    }

    // TODO возвращать неуспех если сервис не выдал результат?
    // TODO переименовать?
    public void create(CardOrder cardOrder){
        kafkaProducer.sendMessage(cardOrder);
    }

}
