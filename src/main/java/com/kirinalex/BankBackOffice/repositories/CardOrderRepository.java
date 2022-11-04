package com.kirinalex.BankBackOffice.repositories;

import com.kirinalex.BankBackOffice.models.CardOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardOrderRepository extends JpaRepository<CardOrder, Integer> {

    List<CardOrder> findByCreatedOnBetween(Date fromDate, Date toDate);

//    public void cardOrders(Data ){
//    }
}
