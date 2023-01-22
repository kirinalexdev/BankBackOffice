package com.kirinalex.BankBackOffice.dao;

import com.kirinalex.BankBackOffice.dto.MonthlyTotalsDTO;
import com.kirinalex.BankBackOffice.dto.TopAgentsByOrdersDTO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

// Используется DAO, а не Repository, для использования @SqlResultSetMapping, и чтобы текст запроса хранить
// в нормально именованном классе, а не прикреплять к Entity через @NamedNativeQuery

@Component
public class CardOrderDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<MonthlyTotalsDTO> monthlyTotals(@Param("fromDate") LocalDateTime fromDate,
                                         @Param("toDate") LocalDateTime toDate,
                                         @Param("currencyRate") double currencyRate) {

        var query = entityManager.createNativeQuery("""
        SELECT
            date_trunc('month', c.created_on) month_begin,
            CAST(SUM(c.credit_limit) / :currencyRate AS NUMERIC(15,2)) credit_limit_sum,
            COUNT(c.id) orders_count
        FROM
            card_orders c
        WHERE
            c.created_on BETWEEN :fromDate AND :toDate
        GROUP BY
            month_begin
        ORDER BY
            month_begin""", "MonthlyTotalsMapping");

        query.setParameter("fromDate", fromDate)
            .setParameter("toDate", toDate)
            .setParameter("currencyRate", currencyRate);

        return query.getResultList();
    }

    public List<TopAgentsByOrdersDTO> topAgentsByOrdersCount(@Param("fromDate") LocalDateTime fromDate,
                                                             @Param("toDate") LocalDateTime toDate) {

        var query = entityManager.createNativeQuery("""
        SELECT
            counts.orders_count,
            counts.credit_limit_sum,
            counts.agent_id,
            employees.first_name,
            employees.last_name
        FROM 
            (SELECT 
                COUNT(c.id) orders_count,
                SUM(c.credit_limit) credit_limit_sum,
                c.agent_id
            FROM 
                card_orders c
            WHERE 
                c.created_on BETWEEN :fromDate AND :toDate
            GROUP BY 
                agent_id
            ORDER BY 
                orders_count DESC
            LIMIT 3) counts
        LEFT JOIN employees 
            ON counts.agent_id = employees.id
        ORDER BY 
            orders_count DESC""", "TopAgentsByOrdersCountMapping");

        query.setParameter("fromDate", fromDate)
            .setParameter("toDate", toDate);

        return query.getResultList();
    }
}
