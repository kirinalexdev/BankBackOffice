package com.kirinalex.BankBackOffice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kirinalex.BankBackOffice.enums.ContactType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contact")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor // нужно, иначе ругается на @Builder  TODO написать какая именно ошибка
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Contact {

    // TODO для почту сделать отдельное поле, чтобы применить @Email,
    //      а этот класс переименовать в Phone

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
