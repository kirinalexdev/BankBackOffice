package com.kirinalex.BankBackOffice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class TopAgentsByOrdersDTO {

    private BigInteger ordersCount;
    private BigDecimal creditLimitSum;
    private Integer agentId;
    private String firstName;
    private String lastName;

    // Этот явный конструктор нужен для @SqlResultSetMapping, для задания нужного порядка параметров конструктора
    public TopAgentsByOrdersDTO(BigInteger ordersCount, BigDecimal creditLimitSum, Integer agentId, String firstName, String lastName) {
        this.ordersCount = ordersCount;
        this.creditLimitSum = creditLimitSum;
        this.agentId = agentId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
