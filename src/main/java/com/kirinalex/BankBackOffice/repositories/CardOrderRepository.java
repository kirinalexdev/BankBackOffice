package com.kirinalex.BankBackOffice.repositories;

import com.kirinalex.BankBackOffice.models.CardOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardOrderRepository extends JpaRepository<CardOrder, Integer> {
}
