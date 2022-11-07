package com.kirinalex.BankBackOffice.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CurrencyRateException.class)
    public ResponseEntity<ErrorResponse> currencyRateExceptionHandler(HttpServletRequest httpRequest, CurrencyRateException ex) {
        HttpStatus status = ex.getStatus();

        var error = new ErrorResponse(new Timestamp(System.currentTimeMillis()),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                httpRequest.getRequestURI());

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> defaultExceptionHandler(HttpServletRequest httpRequest, Exception ex) {
        // TODO ?здесь логировать всё, а в други обработчиках ничего, т.к. мы уже там должны были залогировать на нижних уровнях?
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        var error = new ErrorResponse(new Timestamp(System.currentTimeMillis()),
            status.value(),
            status.getReasonPhrase(),
            ex.getMessage(), // TODO это сообщение логировать, а возврашать какое то типа "ошибка приложения"
            httpRequest.getRequestURI());

        return new ResponseEntity<>(error, status);
    }
}

@Getter
@Setter
@AllArgsConstructor
class ErrorResponse {
    private Timestamp timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
