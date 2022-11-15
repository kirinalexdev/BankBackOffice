package com.kirinalex.BankBackOffice.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kirinalex.BankBackOffice.models.Employee;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CardOrderDTO {

    private int id;

    @NotNull
    private EmployeeDTOId agent;

    @Min(1)
    @NotNull
    private BigDecimal creditLimit;

    @Size(min = 2, max = 100)
    @NotNull
    private String сlient;
}
