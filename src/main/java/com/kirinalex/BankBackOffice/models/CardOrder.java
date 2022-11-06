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

    @Column(name = "credit_limit")
    @NotNull(message = "Кредитный лимит должно быть указан")
    @Min(1)
    private Integer creditLimit;
    // TODO на какой тип заменить, чтобы в постгре был тип numeric
    //      это нужно в частности, чтобы в запросе при делении на это значение не было округления до рублей
    //      -
    //    @Column(nullable= false, precision=7, scale=2)    // Creates the database field with this size.
    //    @Digits(integer=9, fraction=2)                    // Validates data when used as a form
    //    ИЛИ ,
    //    @Column(nullable= false, precision=9, scale=2)
    //    @Digits(integer=7, fraction=2)
    //    https://stackoverflow.com/questions/20907923/jpa-size-annotation-for-bigdecimal
    //    ИЛИ
    //    @Column(columnDefinition = "DECIMAL(7,2)")
    //    -
    //    !!! "It works with "BigDecimal" Java type. But with "Double" does not."
    //    https://stackoverflow.com/questions/4078559/how-to-specify-doubles-precision-on-hibernate
    //    -
    //    @Column(name="Price", columnDefinition="Decimal(10,2) default '100.00'")
    //    -
    //    @Column(precision = 5, scale = 4)
    //    @Type(type = "big_decimal")
    //    private double similarity;
    //    https://stackoverflow.com/questions/4078559/how-to-specify-doubles-precision-on-hibernate

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP) // TODO поэкспериментировать с разными значениями TemporalType - что будет в БД
    private Date createdOn = new Date();

    @Column(name = "сlient")
    @Size(min = 2, max = 100, message = "Поле Клиент должно быть длиной от 2 до 100 символов")
    @NotNull(message = "Клиент должен быть указан") // TODO это надо при наличии @Size? см и в других моделях
    private String сlient;

    // TODO нужны ли message в проверках, или и так сообщения понятные выдаваться будут?

    // TODO если есть @NotNull то нужно ли nullable=false?
    // TODO @NotEmpty нужно ли если задано @Size?

    // TODO задать другие свойства аннтации @Column. в других моделях тоже см
    //    https://docs.oracle.com/javaee/6/api/javax/persistence/Column.html#updatable%28%29
    //    boolean unique() default false;
    //    boolean nullable() default true;
    //    int length() default 255;  - для строки
    //    int precision() default 0; - для числа
    //    int scale() default 0;     - для числа
    //    String columnDefinition() default ""; - The SQL fragment that is used when generating the DDL for the column. Defaults to the generated SQL to create a column of the inferred type.

    // TODO добавить еще каких нибудь полей с хитрыми типами, чтобы показать мое умение работать с ними
    //      в частности @Transient
}
