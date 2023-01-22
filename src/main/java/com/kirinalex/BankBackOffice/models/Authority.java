package com.kirinalex.BankBackOffice.models;

import com.kirinalex.BankBackOffice.utils.ValidationMarker;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(groups = ValidationMarker.OnCreate.class)
    @NotNull(groups = ValidationMarker.OnUpdate.class)
    private Integer id;

    @Column(name = "name")
    @NotEmpty
    @Size(min = 2, max = 100)
    private String name;
}
