package com.kirinalex.BankBackOffice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kirinalex.BankBackOffice.exceptions.ResourceNotFoundException;
import com.kirinalex.BankBackOffice.models.User;
import com.kirinalex.BankBackOffice.services.UserService;
import com.kirinalex.BankBackOffice.exceptions.BadRequestException;
import com.kirinalex.BankBackOffice.utils.UserValidator;
//import com.kirinalex.BankBackOffice.utils.UserValidator2;
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

import static com.kirinalex.BankBackOffice.utils.ErrorsUtil.*;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
@Api(value = "UserController")
public class UserController {

    private final UserValidator userValidator;
    private final UserService userService;

    @PostMapping
    @ApiOperation(value = "Создание пользователя")
    public ResponseEntity<Object> create(@RequestBody @Valid User user,
                                                      BindingResult bindingResult) throws BadRequestException, JsonProcessingException, URISyntaxException {
        userValidator.validate(user, bindingResult);
        checkBindingResult(bindingResult, user);
        userService.save(user);

        return ResponseEntity
                .created(new URI("/user/" + user.getId()))
                .build();
    }

    @PutMapping
    @ApiOperation(value = "Изменение пользователя")
    public ResponseEntity<Object> update(@RequestBody @Valid User user, HttpServletRequest httpRequest,
                                                      BindingResult bindingResult) throws BadRequestException, JsonProcessingException, ResourceNotFoundException {
        checkBindingResult(bindingResult, user);
        findUserOrElseThrow(user.getId());
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ApiOperation(value = "Удаление пользователя")
    public ResponseEntity<Object> delete(@RequestParam int id, HttpServletRequest httpRequest) throws ResourceNotFoundException {
        findUserOrElseThrow(id);
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiOperation(value = "Получение пользователя", response = User.class)
    public ResponseEntity<Object> findById(@RequestParam int id,
                                           HttpServletRequest httpRequest) throws ResourceNotFoundException {
        var user = findUserOrElseThrow(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    private User findUserOrElseThrow(int id) throws ResourceNotFoundException {
        return userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Не найден пользователь по id = " + id));
    }
}