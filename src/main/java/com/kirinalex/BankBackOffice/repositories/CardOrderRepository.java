package com.kirinalex.BankBackOffice.repositories;

import com.kirinalex.BankBackOffice.models.CardOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CardOrderRepository extends JpaRepository<CardOrder, Integer> {

    List<CardOrder> findByCreatedOnBetweenOrderByCreatedOn(LocalDateTime fromDate, LocalDateTime toDate);

}
