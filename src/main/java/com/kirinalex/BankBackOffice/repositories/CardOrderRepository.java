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
    // TODO добавить сортировку и еще разного - см возможности

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
        LIMIT 10) counts
    LEFT JOIN employee 
        ON counts.agent_id = employee.id""")
    List<Map<String, Object>> topAgentsByOrdersCount(@Param("fromDate") Date fromDate,
                                                     @Param("toDate") Date toDate);

    // TODO оставить в credit_limit_sum только 2 цифры после запятой?
    // задал вопрос как это сделать
    // https://ru.stackoverflow.com/questions/1464126/%d0%9a%d0%b0%d0%ba-%d0%b2-postgres-%d0%bf%d1%80%d0%b8%d0%b2%d0%b5%d1%81%d1%82%d0%b8-double-precision-%d0%b2-numeric15-2

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
    List<Map<String, Object>> monthlyTotals(@Param("fromDate") Date fromDate,
                                            @Param("toDate") Date toDate,
                                            @Param("currencyRate") double currencyRate);

}
