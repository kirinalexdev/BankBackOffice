package com.kirinalex.BankBackOffice.repositories;

import com.kirinalex.BankBackOffice.models.CardOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface CardOrderRepository extends JpaRepository<CardOrder, Integer> {

    List<CardOrder> findByCreatedOnBetweenOrderByCreatedOn(LocalDateTime fromDate, LocalDateTime toDate);

    @Query(nativeQuery = true,
    value = """
    SELECT
        counts.orders_count,
        counts.credit_limit_sum,
        counts.agent_id,
        employee.first_name,
        employee.last_name
    FROM 
        (SELECT 
            COUNT(c.id) orders_count,
            SUM(c.credit_limit) credit_limit_sum,
            c.agent_id
        FROM 
            card_order c
        WHERE 
            c.created_on BETWEEN :fromDate AND :toDate
        GROUP BY 
            agent_id
        ORDER BY 
            orders_count DESC
        LIMIT 3) counts
    LEFT JOIN employee 
        ON counts.agent_id = employee.id
    ORDER BY 
        orders_count DESC""")
    List<Map<String, Object>> topAgentsByOrdersCount(@Param("fromDate") LocalDateTime fromDate,
                                                     @Param("toDate") LocalDateTime toDate);

    // todo понять, почему month_begin в реальном вызове вовращает число, а не дату
    @Query(nativeQuery = true,
    value = """
    SELECT
        date_trunc('month', c.created_on) month_begin,
        COUNT(c.id) orders_count,
        CAST(SUM(c.credit_limit) / :currencyRate AS NUMERIC(15,2)) credit_limit_sum
    FROM
        card_order c
    WHERE
        c.created_on BETWEEN :fromDate AND :toDate
    GROUP BY
         month_begin
    ORDER BY
         month_begin""")
    List<Map<String, Object>> monthlyTotals(@Param("fromDate") LocalDateTime fromDate,
                                            @Param("toDate") LocalDateTime toDate,
                                            @Param("currencyRate") double currencyRate);

}
