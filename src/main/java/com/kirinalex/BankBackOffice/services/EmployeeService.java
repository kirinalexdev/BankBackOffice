package com.kirinalex.BankBackOffice.services;

import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.models.Employee;
import com.kirinalex.BankBackOffice.repositories.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public Employee findById(int id) {
        return employeeRepository.findById(id).get();
    }
}
