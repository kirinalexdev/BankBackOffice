package com.kirinalex.BankBackOffice.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    // TODO взамен этого рассмотреть res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    //      https://stackoverflow.com/questions/25422255/how-to-return-404-response-status-in-spring-boot-responsebody-method-return-t
    // TODO перенести в defaultExceptionHandler и там устанавливать статус так как
    //       это делается в ResponseEntityExceptionHandler
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestExceptionHandler(HttpServletRequest httpRequest, BadRequestException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        var error = new ErrorResponse(status, ex, httpRequest);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(CurrencyRateException.class)
    public ResponseEntity<ErrorResponse> currencyRateExceptionHandler(HttpServletRequest httpRequest, CurrencyRateException ex) {
        HttpStatus status = ex.getStatus();
        var error = new ErrorResponse(status, ex, httpRequest);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> defaultExceptionHandler(HttpServletRequest httpRequest, Exception ex) {
        // TODO ?здесь логировать всё, а в други обработчиках ничего, т.к. мы уже там должны были залогировать на нижних уровнях?
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        var error = new ErrorResponse(status, ex, httpRequest);
        return new ResponseEntity<>(error, status);
        //ex.getMessage(), // TODO это сообщение логировать, а возврашать какое то типа "ошибка приложения"
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

    public ErrorResponse(HttpStatus httpStatus, Exception ex, HttpServletRequest httpRequest) {
        timestamp = new Timestamp(System.currentTimeMillis());
        status = httpStatus.value();
        error = httpStatus.getReasonPhrase();
        message = ex.getMessage();
        path = httpRequest.getRequestURI();
    }
}
