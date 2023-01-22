package com.kirinalex.BankBackOffice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirinalex.BankBackOffice.dto.EmployeeDTOId;
import com.kirinalex.BankBackOffice.models.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class UtilConfig {
    // TODO назвать правильно всё
    //      в названии сейчас есть employee, но там могут и другие мапинги добавиться
    @Bean
    public ModelMapper employeeModelMapper(){
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
