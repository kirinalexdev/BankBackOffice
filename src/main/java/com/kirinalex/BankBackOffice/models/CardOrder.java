package com.kirinalex.BankBackOffice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "card_order")
@Getter
@Setter
@Builder
@AllArgsConstructor // нужно, иначе ругается на @Builder
@NoArgsConstructor  // нужно для десериализатора
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
// Заявка
public class CardOrder {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // TODO м.б. есть более подходящее значение? Немчинский рекомендует ид получать в БД
    private int id;                                     //      в других моделях тоже см

    @ManyToOne
    @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable=false)
    @NotNull
    private Employee agent;

    // TODO как валидировать, что передан несуществующий ключ:
    //      ОШИБКА: INSERT или UPDATE в таблице "card_order" нарушает ограничение внешнего ключа "fkcs5aefchaqji91dkqvnb9wgak"
    //      Подробности: Ключ (agent_id)=(10) отсутствует в таблице "employee".

    @Column(name = "credit_limit", precision = 15, scale = 2, nullable = false)
    @Min(1)
    @NotNull
    private BigDecimal creditLimit;

    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "сlient", length = 100, nullable = false)
    @Size(min = 2, max = 100)
    @NotNull
    private String сlient;

    @Override
    public String toString() {
        return "CardOrder{" +
                "id=" + id +
                ", agent=" + agent +
                ", creditLimit=" + creditLimit +
                ", createdOn=" + createdOn +
                ", сlient='" + сlient + '\'' +
                '}';
    }
}
