package com.kirinalex.BankBackOffice.controllers;

import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.models.Contact;
import com.kirinalex.BankBackOffice.models.Employee;
import com.kirinalex.BankBackOffice.services.EmployeeService;
import com.kirinalex.BankBackOffice.utils.BadRequestException;
import com.kirinalex.BankBackOffice.utils.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.kirinalex.BankBackOffice.utils.ErrorsUtil.generateErrorMessage;

@RestController
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public void create(@RequestBody @Valid Employee employee,
                       BindingResult bindingResult) throws BadRequestException {

        if (bindingResult.hasErrors()) {
            var errorMessage = generateErrorMessage(bindingResult.getFieldErrors());
            throw new BadRequestException(errorMessage);
        }

        employeeService.save(employee);
    }

    @PutMapping
    public void update(@RequestBody @Valid Employee employee,
                       BindingResult bindingResult) throws BadRequestException {

        if (bindingResult.hasErrors()) {
            var s = generateErrorMessage(bindingResult.getFieldErrors());
            throw new BadRequestException(s);
        }

        employeeService.save(employee);
    }

    @DeleteMapping
    public void delete(@RequestParam int id) {
        employeeService.delete(id);
    }

    @GetMapping("/find-by-id")
    public ResponseEntity<Object> findbyid(@RequestParam int id, HttpServletRequest httpRequest) {
        var optionalEmployee = employeeService.findById(id);

        if (!optionalEmployee.isPresent()) {
            var status = HttpStatus.NOT_FOUND;
            var error =  new ErrorResponse(status, "Не найден сотрудник с id = " + id, httpRequest);
            return new ResponseEntity<>(error, status);
        }
        return new ResponseEntity<>(optionalEmployee.get(), HttpStatus.OK);
    }
}
