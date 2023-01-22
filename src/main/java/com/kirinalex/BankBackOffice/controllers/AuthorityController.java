package com.kirinalex.BankBackOffice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kirinalex.BankBackOffice.models.Authority;
import com.kirinalex.BankBackOffice.models.User;
import com.kirinalex.BankBackOffice.services.AuthorityService;
import com.kirinalex.BankBackOffice.services.UserService;
import com.kirinalex.BankBackOffice.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import static com.kirinalex.BankBackOffice.utils.ErrorsUtil.*;

@Controller
@RequestMapping("/authority")
@AllArgsConstructor
@Slf4j
@Validated
@Api(value = "AuthorityController")
public class AuthorityController {

    private final AuthorityValidator authorityValidator;
    private final AuthorityService authorityService;

    @PostMapping
    @Validated(ValidationMarker.OnCreate.class)
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
    @Validated(ValidationMarker.OnUpdate.class)
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

        return ResponseEntity.ok(authority);
    }

    private ResponseEntity<Object> errorResponseNotFound(int idAuthority, HttpServletRequest httpRequest){
        var status = HttpStatus.NOT_FOUND;
        var error =  new ErrorResponse(status, "Не найдено authority с id = " + idAuthority, httpRequest);
        return ResponseEntity.status(status).body(error);
    }
}