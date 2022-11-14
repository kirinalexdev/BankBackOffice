package com.kirinalex.BankBackOffice.services;

import com.kirinalex.BankBackOffice.models.Contact;
import com.kirinalex.BankBackOffice.models.Employee;
import com.kirinalex.BankBackOffice.repositories.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public void save(Employee employee) {
        var contacts = employee.getContacts();
        for (Contact contact: contacts) {
            contact.setEmployee(employee);
        }

        employeeRepository.save(employee);
    }

    public Optional<Employee> findById(int id) {
        return employeeRepository.findById(id);
    }

    @Transactional
    public void delete(int id){
        employeeRepository.deleteById(id);
    }

}
