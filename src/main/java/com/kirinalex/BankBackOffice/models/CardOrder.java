package com.kirinalex.BankBackOffice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kirinalex.BankBackOffice.dto.MonthlyTotalsDTO;
import com.kirinalex.BankBackOffice.dto.TopAgentsByOrdersDTO;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@SqlResultSetMapping(name = "MonthlyTotalsMapping",
        classes = {@ConstructorResult(targetClass = MonthlyTotalsDTO.class,
            columns = {
                @ColumnResult(name = "month_begin", type = LocalDateTime.class),
                @ColumnResult(name = "orders_count", type = BigInteger.class),
                @ColumnResult(name = "credit_limit_sum", type = BigDecimal.class)})})

@SqlResultSetMapping(name = "TopAgentsByOrdersCountMapping",
        classes = {@ConstructorResult(targetClass = TopAgentsByOrdersDTO.class,
            columns = {
                @ColumnResult(name = "orders_count", type = BigInteger.class),
                @ColumnResult(name = "credit_limit_sum", type = BigDecimal.class),
                @ColumnResult(name = "agent_id", type = Integer.class),
                @ColumnResult(name = "first_name", type = String.class),
                @ColumnResult(name = "last_name", type = String.class)})})

@Entity
@Table(name = "card_order")
@Getter
@Setter
@Builder
@AllArgsConstructor // нужно для @Builder
@NoArgsConstructor  // нужно для десериализатора
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CardOrder {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable=false)
    @NotNull
    private Employee agent;

    @Column(name = "credit_limit", precision = 15, scale = 2, nullable = false)
    @Min(1)
    @NotNull
    private BigDecimal creditLimit;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "client", length = 100, nullable = false)
    @Size(min = 2, max = 100)
    @NotNull
    private String client;

    // @ToString провоцирует бесконечную рекурсию
    @Override
    public String toString() {
        return "CardOrder{" +
                "id=" + id +
                ", agent=" + agent +
                ", creditLimit=" + creditLimit +
                ", createdOn=" + createdOn +
                ", client='" + client + '\'' +
                '}';
    }
}
