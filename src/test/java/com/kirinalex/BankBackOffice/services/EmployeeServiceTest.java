package com.kirinalex.BankBackOffice.services;

import com.kirinalex.BankBackOffice.models.Contact;
import com.kirinalex.BankBackOffice.models.Employee;
import com.kirinalex.BankBackOffice.repositories.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void givenEmployee_whenSave_thenRepositorySave() {
        // given
        var contacts = new ArrayList<Contact>();
        contacts.add(new Contact());
        contacts.add(new Contact());

        var employee = Employee.builder()
                .contacts(contacts)
                .build();

        // when
        employeeService.save(employee);

        // then
        verify(employeeRepository).save(employee);
        assertThat(contacts).allMatch(contact -> contact.getEmployee() == employee);
    }

    @Test
    void givenEmployeeId_whenDelete_thenRepositoryDelete() {
        // given
        var employeeId = 2;

        // when
        employeeService.delete(employeeId);

        // then
        verify(employeeRepository).deleteById(employeeId);
    }

    @Test
    void givenEmployeeId_whenFindById_thenReturnEmployee() {
        // given
        var employeeId = 2;
        var optionalEmployee = Optional.of(new Employee());
        given(employeeRepository.findById(employeeId))
                .willReturn(optionalEmployee);

        // when
        var optionalFoundEmployee = employeeService.findById(employeeId);

        // then
        verify(employeeRepository).findById(employeeId);
        assertThat(optionalFoundEmployee).isEqualTo(optionalEmployee);
    }
}