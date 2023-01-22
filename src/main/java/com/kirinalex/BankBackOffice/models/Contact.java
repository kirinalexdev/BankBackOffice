package com.kirinalex.BankBackOffice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kirinalex.BankBackOffice.enums.ContactType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "contact")
@Data
@Builder
@ToString(exclude = "employee") // exclude - исключаем бесконечную рекурсию
@AllArgsConstructor // нужно для @Builder
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Contact {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "value", length = 100, nullable = false)
    @Size(min = 5, max = 100)
    @NotNull
    private String value;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable=false)
    @NotNull
    private Employee employee;

    @Column(name = "type", length = 10, nullable=false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private ContactType type;
}
