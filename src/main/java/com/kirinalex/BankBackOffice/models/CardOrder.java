package com.kirinalex.BankBackOffice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

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
