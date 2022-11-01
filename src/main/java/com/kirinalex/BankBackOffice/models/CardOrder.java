package com.kirinalex.BankBackOffice.models;

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
// Заявка
public class CardOrder {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // TODO м.б. есть более подходящее значение? Немчинский рекомендует ид получать в БД
    private int id;

    // TODO сделать @ManyToOne, тип поля сделать объектным, и всё остальное переделать соответственно
    @Column(name = "agent")
    private String agent;

    // TODO переделать на перечисление
    @Column(name = "card_type")
    private String cardType;

    // дата время
//    @Column(name = "card_type")
//    private String cardType;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP) // TODO для чего это?
    private Date createdOn = new Date();

    @Column(name = "сlient")
    private String сlient;

    // TODO задать другие свойства аннтации @Column
    // TODO задать другие аннтации: @NotEmpty, @NotNull, @Size, @DateTimeFormat, @Temporal и др.

}
