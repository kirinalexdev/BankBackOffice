package com.kirinalex.BankBackOffice.repositories;

import com.kirinalex.BankBackOffice.models.CardOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface CardOrderRepository extends JpaRepository<CardOrder, Integer> {

    List<CardOrder> findByCreatedOnBetween(Date fromDate, Date toDate);

    @Query(nativeQuery = true,
    value = """
    SELECT
        counts.ordersCount,
        counts.agent_id,
        employee.first_name,
        employee.last_name
    FROM 
        (SELECT 
            count(c.id) ordersCount,
            c.agent_id
        FROM 
            card_order c
        where 
            c.created_on between :fromDate and :toDate
        GROUP BY 
            agent_id
        ORDER BY 
            ordersCount DESC                 
        LIMIT 10) counts
    LEFT JOIN employee 
        on counts.agent_id = employee.id""")
    List<Map<String, Object>> topAgentsByOrdersCount(@Param("fromDate") Date fromDate,
                                                     @Param("toDate") Date toDate);

}
