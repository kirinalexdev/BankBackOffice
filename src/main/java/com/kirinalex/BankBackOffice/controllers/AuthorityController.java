package com.kirinalex.BankBackOffice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kirinalex.BankBackOffice.models.Authority;
import com.kirinalex.BankBackOffice.models.User;
import com.kirinalex.BankBackOffice.services.AuthorityService;
import com.kirinalex.BankBackOffice.services.UserService;
import com.kirinalex.BankBackOffice.utils.AuthorityValidator;
import com.kirinalex.BankBackOffice.utils.BadRequestException;
import com.kirinalex.BankBackOffice.utils.ErrorResponse;
import com.kirinalex.BankBackOffice.utils.UserValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import static com.kirinalex.BankBackOffice.utils.ErrorsUtil.generateErrorMessage;

@Controller
@RequestMapping("/authority")
@AllArgsConstructor
@Slf4j
@Api(value = "AuthorityController")
public class AuthorityController {

    private final AuthorityValidator authorityValidator;
    private final AuthorityService authorityService;

    @PostMapping
    @ApiOperation(value = "Добавление authority")
    public ResponseEntity<Object> create(@RequestBody @Valid Authority authority,
                                                      BindingResult bindingResult) throws BadRequestException, JsonProcessingException, URISyntaxException {
        authorityValidator.validate(authority, bindingResult);
        checkBindingResult(bindingResult, authority);

        authorityService.save(authority);

        return ResponseEntity
                .created(new URI("/authority/" + authority.getId()))
                .build();
    }

    @PutMapping
    @ApiOperation(value = "Изменение authority")
    public ResponseEntity<Object> update(@RequestBody @Valid Authority authority, HttpServletRequest httpRequest,
                                                      BindingResult bindingResult) throws BadRequestException, JsonProcessingException {
        checkBindingResult(bindingResult, authority);

        var id = authority.getId();
        var foundAuthority = authorityService.findById(id).orElse(null);

        if (foundAuthority == null) {
            return errorResponseNotFound(id, httpRequest);
        }

        authorityService.save(authority);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ApiOperation(value = "Удаление authority")
    public ResponseEntity<Object> delete(@RequestParam int id, HttpServletRequest httpRequest) {
        var authority = authorityService.findById(id).orElse(null);

        if (authority == null) {
            return errorResponseNotFound(id, httpRequest);
        }

        authorityService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiOperation(value = "Получение authority", response = Authority.class)
    public ResponseEntity<Object> findById(@RequestParam int id,
                                           HttpServletRequest httpRequest) {
        var authority = authorityService.findById(id).orElse(null);

        if (authority == null) {
            return errorResponseNotFound(id, httpRequest);
        }

        return new ResponseEntity<>(authority, HttpStatus.OK);
    }

    protected void checkBindingResult(BindingResult bindingResult, Authority authority) throws BadRequestException, JsonProcessingException {
        if (bindingResult.hasErrors()) {
            var message = generateErrorMessage(bindingResult.getFieldErrors());
            log.error("{}. {}", message, authority, new Throwable());
            throw new BadRequestException(message);
        }
    }

    private ResponseEntity<Object> errorResponseNotFound(int idAuthority, HttpServletRequest httpRequest){
        var status = HttpStatus.NOT_FOUND;
        var error =  new ErrorResponse(status, "Не найдено authority с id = " + idAuthority, httpRequest);
        return new ResponseEntity<>(error, status);
    }
}