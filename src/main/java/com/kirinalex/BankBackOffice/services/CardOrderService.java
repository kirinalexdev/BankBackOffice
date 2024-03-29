package com.kirinalex.BankBackOffice.services;

import com.kirinalex.BankBackOffice.dao.CardOrderDAO;
import com.kirinalex.BankBackOffice.dto.MonthlyTotalsDTO;
import com.kirinalex.BankBackOffice.dto.TopAgentsByOrdersDTO;
import com.kirinalex.BankBackOffice.kafka.KafkaProducer;
import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.repositories.CardOrderRepository;
import com.kirinalex.BankBackOffice.finance.Currency;
import com.kirinalex.BankBackOffice.exceptions.CurrencyRateException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CardOrderService {

    private KafkaProducer kafkaProducer;
    private final CardOrderRepository cardOrderRepository;
    private final CardOrderDAO cardOrderDAO;
    private final Currency currency;

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

    public List<TopAgentsByOrdersDTO> topAgentsByOrdersCount(LocalDateTime fromDate, LocalDateTime toDate) {
        return cardOrderDAO.topAgentsByOrdersCount(fromDate, toDate.plusNanos(999999999));
    }

    public List<MonthlyTotalsDTO> monthlyTotals(LocalDateTime fromDate, LocalDateTime toDate, String currencyCode) throws CurrencyRateException {
        double currencyRate = currency.currencyRate(currencyCode);
        return cardOrderDAO.monthlyTotals(fromDate, toDate, currencyRate);
    }
}
