package com.kirinalex.BankBackOffice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirinalex.BankBackOffice.dto.EmployeeDTO;
import com.kirinalex.BankBackOffice.models.Employee;
import com.kirinalex.BankBackOffice.services.EmployeeService;
import com.kirinalex.BankBackOffice.utils.BadRequestException;
import com.kirinalex.BankBackOffice.utils.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
@Slf4j
public class EmployeeController {
    private final EmployeeService employeeService;
    private final ModelMapper employeeModelMapper;
    private final ObjectMapper objectMapper;

    @PostMapping
    public void create(@RequestBody @Valid EmployeeDTO employeeDTO,
                       BindingResult bindingResult) throws Exception {

        checkBindingResult(bindingResult, employeeDTO);

        var employee = employeeModelMapper.map(employeeDTO, Employee.class);
        employeeService.save(employee);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody @Valid EmployeeDTO employeeDTO,
                       HttpServletRequest httpRequest,
                       BindingResult bindingResult) throws BadRequestException, JsonProcessingException {

        checkBindingResult(bindingResult, employeeDTO);

        var id = employeeDTO.getId();
        var employee = employeeService.findById(id).orElse(null);

        if (employee == null) {
            return errorResponseNotFound(id, httpRequest);
        }

        employeeModelMapper.map(employeeDTO, employee);
        employeeService.save(employee);

        return new ResponseEntity<>(null, HttpStatus.OK);

        // TODO Сделать возможность удаления контактов. Сейчас же используется версия 2.1.1 ModelMapper,
        //      в которой нет такошго свойства modelMapper.getConfiguration().setCollectionsMergeEnabled(false);
        //      Причина по которой используется старая версия описана здсь привестим эту ссылку https://github.com/modelmapper/modelmapper/issues/479
        // TODO Сделать возможность обновления контактов. Воможно сделать отдельный API для контактов.
    }

    @DeleteMapping
    public void delete(@RequestParam int id) {
        employeeService.delete(id);
    }

    @GetMapping("/find-by-id")
    public ResponseEntity<Object> findbyid(@RequestParam int id,
                                           HttpServletRequest httpRequest) {
        var employee = employeeService.findById(id).orElse(null);

        if (employee == null) {//TODO логировать? с каким уровнем?
            return errorResponseNotFound(id, httpRequest);
        }

        var employeeDTO= employeeModelMapper.map(employee, EmployeeDTO.class);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    private void checkBindingResult (BindingResult bindingResult, EmployeeDTO employeeDTO) throws BadRequestException, JsonProcessingException {
        if (bindingResult.hasErrors()) {
            var message = generateErrorMessage(bindingResult.getFieldErrors());
            log.error("{}. employeeDTO:{}", message, objectMapper.writeValueAsString(employeeDTO), new Throwable());
            throw new BadRequestException(message);
        }

    }

    private ResponseEntity<Object> errorResponseNotFound(int idEmployee, HttpServletRequest httpRequest){
        var status = HttpStatus.NOT_FOUND;
        var error =  new ErrorResponse(status, "Не найден сотрудник с id = " + idEmployee, httpRequest);
        return new ResponseEntity<>(error, status);
    }
}
