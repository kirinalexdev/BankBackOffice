package com.kirinalex.BankBackOffice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    private Employee agent;

    @Column(name = "credit_limit")
    private int creditLimit; // TODO на какой тип заменить, чтобы в постгре был тип numeric
                             // это нужно в частности, чтобы в запросе при делении на это значение не было округления до рублей
    // TODO добавить поле Валюта

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP) // TODO для чего это?
    private Date createdOn = new Date();

    @Column(name = "сlient")
    private String сlient;

    // TODO задать другие свойства аннтации @Column. в других моделях тоже см
    // TODO задать другие аннтации: @NotEmpty, @NotNull, @Size, @DateTimeFormat, @Temporal и др.  в других моделях тоже см

    // TODO добавить еще каких нибудь полей с хитрыми типами, чтобы показать мое умение работать с ними
    //      в частности @Transient
}
