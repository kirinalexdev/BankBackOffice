package com.kirinalex.BankBackOffice.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor // нужно для @Builder
@NoArgsConstructor
@EqualsAndHashCode
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class EmployeeDTOId {

    private int id;

}
