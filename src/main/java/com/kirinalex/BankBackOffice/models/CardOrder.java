package com.kirinalex.BankBackOffice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "card_order")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor // нужно, иначе ругается на @Builder
@NoArgsConstructor  // нужно для десериализатора
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
// Заявка
public class CardOrder {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // TODO м.б. есть более подходящее значение? Немчинский рекомендует ид получать в БД
    private int id;                                     // в других моделях тоже см

    // TODO тут нужно @NotNull и/или nullable=false? см и в других моделях
    @ManyToOne
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    private Employee agent;

    @Column(name = "credit_limit", precision = 15, scale = 2, nullable = false)
    @NotNull(message = "Кредитный лимит должно быть указан")
    @Min(1)
    private BigDecimal creditLimit;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn = new Date();

    @Column(name = "сlient", length = 100)
    @Size(min = 2, max = 100, message = "Поле Клиент должно быть длиной от 2 до 100 символов")
    @NotNull(message = "Клиент должен быть указан") // TODO это надо при наличии @Size? см и в других моделях
    private String сlient;

    // TODO нужны ли message в проверках, или и так сообщения понятные выдаваться будут?

    // TODO если есть @NotNull то нужно ли nullable=false?
    //      или наоборот если есть nullable=false то нужно ли @NotNull? nullable=falseг нужно для прямых вставок минуя модель?

    // TODO @NotEmpty нужно ли если задано @Size?
}
