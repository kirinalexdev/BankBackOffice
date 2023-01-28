package com.kirinalex.BankBackOffice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kirinalex.BankBackOffice.dto.EmployeeDTO;
import com.kirinalex.BankBackOffice.exceptions.ResourceNotFoundException;
import com.kirinalex.BankBackOffice.models.Employee;
import com.kirinalex.BankBackOffice.services.EmployeeService;
import com.kirinalex.BankBackOffice.exceptions.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;

import static com.kirinalex.BankBackOffice.utils.ErrorsUtil.*;

@RestController
@RequestMapping(value = "/v1/employee", produces = "application/json") // produces для swagger
@Setter
@AllArgsConstructor
@Slf4j
@Api(value = "EmployeeController")
public class EmployeeController {

    private EmployeeService employeeService;
    private ModelMapper employeeModelMapper;

    @PostMapping
    @ApiOperation(value = "Добавление сотрудника")
    public ResponseEntity<Object> create(@RequestBody @Valid EmployeeDTO employeeDTO,
                       BindingResult bindingResult) throws BadRequestException, JsonProcessingException, URISyntaxException {
        checkBindingResult(bindingResult, employeeDTO);
        var employee = employeeModelMapper.map(employeeDTO, Employee.class);
        employeeService.save(employee);

        return ResponseEntity
                .created(new URI("/v1/employee/" + employee.getId()))
                .build();
    }

    @PutMapping
    @ApiOperation(value = "Изменение данных сотрудника")
    public ResponseEntity<Object> update(@RequestBody @Valid EmployeeDTO employeeDTO,
                                         BindingResult bindingResult,
                                         HttpServletRequest httpRequest) throws BadRequestException, JsonProcessingException, ResourceNotFoundException {

        checkBindingResult(bindingResult, employeeDTO);
        var employee = findEmployeeOrElseThrow(employeeDTO.getId());
        employeeModelMapper.map(employeeDTO, employee);
        employeeService.save(employee);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ApiOperation(value = "Удаление сотрудника")
    public ResponseEntity<Object> delete(@RequestParam int id, HttpServletRequest httpRequest) throws ResourceNotFoundException {
        findEmployeeOrElseThrow(id);
        employeeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiOperation(value = "Получение сотрудника", response = EmployeeDTO.class)
    public ResponseEntity<Object> findById(@RequestParam int id,
                                           HttpServletRequest httpRequest) throws ResourceNotFoundException {
        var employee = findEmployeeOrElseThrow(id);
        var employeeDTO= employeeModelMapper.map(employee, EmployeeDTO.class);
        return ResponseEntity.ok(employeeDTO);
    }

    private Employee findEmployeeOrElseThrow(int id) throws ResourceNotFoundException {
        return employeeService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Не найден сотрудник по id = " + id));
    }
}
