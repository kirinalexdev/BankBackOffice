package com.kirinalex.BankBackOffice.controllers;

import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.models.Employee;
import com.kirinalex.BankBackOffice.services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/create")
    // TODO добавить другие параметры к @RequestParam? см и другие контроллеры
    // TODO добавить @Valid, BindingResult и прочее см и другие контроллеры
    public void create(@RequestBody Employee employee){
        employeeService.save(employee);
    }

    // TODO надо ли это?
    @GetMapping("/find-by-id")
    public Employee get(@RequestParam int id){
        return employeeService.findById(id);
    }

}
