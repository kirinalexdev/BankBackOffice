package com.kirinalex.BankBackOffice.utils;

import lombok.extern.slf4j.Slf4j;
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
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(CurrencyRateException.class)
    public ResponseEntity<ErrorResponse> currencyRateExceptionHandler(HttpServletRequest httpRequest, CurrencyRateException ex) {
        var status = ex.getStatus();
        var error = new ErrorResponse(status, ex.getMessage(), httpRequest);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> defaultExceptionHandler(HttpServletRequest httpRequest, Exception ex) {
        log.error(ex.getMessage(), ex);

        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var error = new ErrorResponse(status, "Ошибка приложения", httpRequest);
        return new ResponseEntity<>(error, status);
    }
}

