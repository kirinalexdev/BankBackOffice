package com.kirinalex.BankBackOffice.models;

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
public class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    //!!! как это реализовать. с учтом того, что не все сотрудники являются агентами
    //*@Cascade(org.hibernate.annotations.CascadeType.ALL) // TODO что тут?
    //@OneToMany(mappedBy = "agent", cascade = CascadeType.)                      // TODO или использовать свойство cascade = CascadeType.ALL у @OneToMany?
    //private List<CardOrder> cardOrders;                 // TODO см orphanRemoval = true
}
