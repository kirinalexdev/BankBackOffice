package com.kirinalex.BankBackOffice.services;

import com.kirinalex.BankBackOffice.kafka.KafkaProducer;
import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.repositories.CardOrderRepository;
import com.kirinalex.BankBackOffice.finance.Currency;
import com.kirinalex.BankBackOffice.utils.CurrencyRateException;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

// ТОДО все изменения БД сделать через кафку

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CardOrderService {
    private KafkaProducer kafkaProducer;
    private final CardOrderRepository cardOrderRepository;

    @Transactional
    public void save(CardOrder cardOrder) {
        cardOrderRepository.save(cardOrder);
    }

    public void create(CardOrder cardOrder){
        cardOrder.setCreatedOn(LocalDateTime.now());
        kafkaProducer.sendMessage(cardOrder);
    }

    public void update(CardOrder cardOrder) {
        kafkaProducer.sendMessage(cardOrder);
    }

    @Transactional
    public void delete(int id){
        cardOrderRepository.deleteById(id);
    }

    public Optional<CardOrder> findById(int id) {
        return cardOrderRepository.findById(id);
    }

    public List<CardOrder> findByCreatedOnBetweenOrderByCreatedOn(LocalDateTime fromDate, LocalDateTime toDate) {
        return cardOrderRepository.findByCreatedOnBetweenOrderByCreatedOn(fromDate, toDate.plusNanos(999999999));
    }

    public List<Map<String, Object>> topAgentsByOrdersCount(LocalDateTime fromDate, LocalDateTime toDate) {
        return cardOrderRepository.topAgentsByOrdersCount(fromDate, toDate.plusNanos(999999999));
    }

    public List<Map<String, Object>> monthlyTotals(LocalDateTime fromDate, LocalDateTime toDate, String currency) throws CurrencyRateException {
        double currencyRate = Currency.currencyRate(currency);
        return cardOrderRepository.monthlyTotals(fromDate, toDate, currencyRate);
    }
}
