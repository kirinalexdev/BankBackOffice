package com.kirinalex.BankBackOffice.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Data
public class ErrorResponse {

    private Timestamp timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ErrorResponse(HttpStatus httpStatus, String message, HttpServletRequest httpRequest) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.path = httpRequest.getRequestURI(); // TODO сюда передавать только URI, без httpRequest?
    }
}
