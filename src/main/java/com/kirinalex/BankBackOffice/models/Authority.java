package com.kirinalex.BankBackOffice.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "authorities")
public class Authority {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty
    @Size(min = 2, max = 100)
    private String name;
}
