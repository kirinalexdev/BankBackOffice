package com.kirinalex.BankBackOffice.config;

import com.kirinalex.BankBackOffice.dto.EmployeeDTOId;
import com.kirinalex.BankBackOffice.models.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {
// TODO назвать правильно всё
//      в названии сейчас есть employee, но там могут и другие мапинги добавиться

    //@Bean("employeeModelMapper")
    @Bean
    public ModelMapper employeeModelMapper(){
        var modelMapper = new ModelMapper();
        //modelMapper.createTypeMap(EmployeeDTOId.class, Employee.class);
        return modelMapper;
    }

//    @Bean("employeeModelMapper2")
//    public ModelMapper employeeModelMapper2(){
//        var modelMapper = new ModelMapper();
//        modelMapper.createTypeMap(Employee.class, EmployeeDTO.class);
//        return modelMapper;
//    }
}
