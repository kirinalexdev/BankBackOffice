package com.kirinalex.BankBackOffice.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kirinalex.BankBackOffice.utils.ValidationMarker;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiResponses;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString
public class EmployeeDTO {

    @Null(groups = ValidationMarker.OnCreate.class)
    @NotNull(groups = ValidationMarker.OnUpdate.class)
    private Integer id;

    @Size(min = 2, max = 100)
    @NotNull
    private String firstName;

    @Size(min = 2, max = 100)
    @NotNull
    private String lastName;

    private List<ContactDTO> contacts = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")

    @ApiModelProperty(example = "2021-08-20") // без этого swagger отображает сложную структуру для LocalDate
    @NotNull
    private LocalDate birthday;
}
