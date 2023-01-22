package com.kirinalex.BankBackOffice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kirinalex.BankBackOffice.exceptions.BadRequestException;
import com.kirinalex.BankBackOffice.exceptions.ResourceNotFoundException;
import com.kirinalex.BankBackOffice.models.Authority;
import com.kirinalex.BankBackOffice.services.AuthorityService;
import com.kirinalex.BankBackOffice.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import static com.kirinalex.BankBackOffice.utils.ErrorsUtil.*;

@Controller
@RequestMapping("/v1/authority")
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
                                                      BindingResult bindingResult) throws BadRequestException, JsonProcessingException, ResourceNotFoundException {
        checkBindingResult(bindingResult, authority);
        findAuthorityOrElseThrow(authority.getId());
        authorityService.save(authority);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ApiOperation(value = "Удаление authority")
    public ResponseEntity<Object> delete(@RequestParam int id, HttpServletRequest httpRequest) throws ResourceNotFoundException {
        findAuthorityOrElseThrow(id);
        authorityService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiOperation(value = "Получение authority", response = Authority.class)
    public ResponseEntity<Object> findById(@RequestParam int id,
                                           HttpServletRequest httpRequest) throws ResourceNotFoundException {
        var authority = findAuthorityOrElseThrow(id);
        return ResponseEntity.ok(authority);
    }

    private Authority findAuthorityOrElseThrow(int id) throws ResourceNotFoundException {
        return authorityService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Не найдено authority по id = " + id));
    }

}