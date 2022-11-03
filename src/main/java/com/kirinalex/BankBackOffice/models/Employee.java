package com.kirinalex.BankBackOffice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "employee")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor // нужно, иначе ругается на @Builder
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // TODO решает ошибку бесконечной рекурсии при сериализации, но может как по другому сделать чтобы сериализовался только id дочернего объекта а не весь
public class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL) // TODO orphanRemoval = true ?
    private List<Contact> contacts;
}
