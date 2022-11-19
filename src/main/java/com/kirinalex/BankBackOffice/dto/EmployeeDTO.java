package com.kirinalex.BankBackOffice.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class EmployeeDTO {

    private int id;

    @Size(min = 2, max = 100)
    @NotNull
    private String firstName;

    @Size(min = 2, max = 100)
    @NotNull
    private String lastName;

    private List<ContactDTO> contacts = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date birthday;
}
