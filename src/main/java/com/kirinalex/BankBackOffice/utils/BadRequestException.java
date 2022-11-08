package com.kirinalex.BankBackOffice.utils;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends Exception{

    public BadRequestException(String message) {
        super(message);
    }
}
