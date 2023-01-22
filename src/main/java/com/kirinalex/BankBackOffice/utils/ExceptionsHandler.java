package com.kirinalex.BankBackOffice.utils;

import com.kirinalex.BankBackOffice.exceptions.BadRequestException;
import com.kirinalex.BankBackOffice.exceptions.CurrencyRateException;
import com.kirinalex.BankBackOffice.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestExceptionHandler(HttpServletRequest httpRequest, BadRequestException ex) {
        var status = HttpStatus.BAD_REQUEST;
        var error = new ErrorResponse(status, ex.getMessage(), httpRequest);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(CurrencyRateException.class)
    public ResponseEntity<ErrorResponse> currencyRateExceptionHandler(HttpServletRequest httpRequest, CurrencyRateException ex) {
        var status = ex.getStatus();
        var error = new ErrorResponse(status, ex.getMessage(), httpRequest);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationExceptionHandler(HttpServletRequest httpRequest, ConstraintViolationException ex) {
        var status = HttpStatus.BAD_REQUEST;
        var error = new ErrorResponse(status, ex.getMessage(), httpRequest);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundExceptionHandler(HttpServletRequest httpRequest, ResourceNotFoundException ex) {
        var status = HttpStatus.NOT_FOUND;
        var error = new ErrorResponse(status, ex.getMessage(), httpRequest);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> defaultExceptionHandler(HttpServletRequest httpRequest, Exception ex) {
        log.error(ex.getMessage(), ex);

        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var error = new ErrorResponse(status, "Ошибка приложения", httpRequest);
        return ResponseEntity.status(status).body(error);
    }
}

