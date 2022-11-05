package com.kirinalex.BankBackOffice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirinalex.BankBackOffice.kafka.KafkaProducer;
import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.repositories.CardOrderRepository;
import com.kirinalex.BankBackOffice.utils.Currency;
import com.kirinalex.BankBackOffice.utils.CurrencyRateException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CardOrderService {

    private KafkaProducer kafkaProducer;
    private final CardOrderRepository cardOrderRepository;

    @Transactional
    public void save(CardOrder cardOrder) {
        cardOrderRepository.save(cardOrder);
    }

    // TODO удалить этот метод? реально он нужен?
    @Transactional(readOnly = true)
    public CardOrder findById(int id) {
        // TODO или findOne?
        // TODO верно так получать значение?
        // TODO как проверять на наличие?
        // TODO нормально ли что тут возвращается и агент весь а не только его id? может быть изменить fetch = FetchType.. или типа того?
        return cardOrderRepository.findById(id).get();
    }

    // TODO возвращать неуспех если сервис не выдал результат?
    // TODO переименовать
    public void create(CardOrder cardOrder){
        kafkaProducer.sendMessage(cardOrder);
    }

    public List<CardOrder> findByCreatedOnBetween(Date fromDate, Date toDate) {
        return cardOrderRepository.findByCreatedOnBetween(fromDate, toDate);
    }

    public List<Map<String, Object>> topAgentsByOrdersCount(Date fromDate, Date toDate) {
        return cardOrderRepository.topAgentsByOrdersCount(fromDate, toDate);
    }

    public List<Map<String, Object>> monthlyTotals(Date fromDate, Date toDate, String currency) throws CurrencyRateException {
        double currencyRate = Currency.currencyRate(currency);
        return cardOrderRepository.monthlyTotals(fromDate, toDate, currencyRate);
    }


}
