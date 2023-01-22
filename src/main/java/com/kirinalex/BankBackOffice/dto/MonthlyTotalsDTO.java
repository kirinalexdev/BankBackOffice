package com.kirinalex.BankBackOffice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor // нужно для @Builder
public class MonthlyTotalsDTO {

    @ApiModelProperty(example = "2021-08-20T00:00:00") // без этого swagger отображает сложную структуру для LocalDateTime
    private LocalDateTime monthBegin;
    private BigInteger ordersCount;
    private BigDecimal creditLimit;

    // Этот явный конструктор нужен для @SqlResultSetMapping, для задания нужного порядка параметров конструктора
    public MonthlyTotalsDTO(LocalDateTime monthBegin, BigDecimal creditLimit, BigInteger ordersCount) {
        this.monthBegin = monthBegin;
        this.ordersCount = ordersCount;
        this.creditLimit = creditLimit;
    }
}
