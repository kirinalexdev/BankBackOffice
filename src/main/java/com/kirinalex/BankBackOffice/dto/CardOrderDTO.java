package com.kirinalex.BankBackOffice.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kirinalex.BankBackOffice.utils.ValidationMarker;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor // нужно для @Builder
@NoArgsConstructor
@EqualsAndHashCode
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CardOrderDTO {

    @Null(groups = ValidationMarker.OnCreate.class)
    @NotNull(groups = ValidationMarker.OnUpdate.class)
    private Integer id;

    @NotNull
    private EmployeeDTOId agent;

    @Min(1)
    @NotNull
    private BigDecimal creditLimit;

    @Size(min = 2, max = 100)
    @NotNull
    private String client;
}
