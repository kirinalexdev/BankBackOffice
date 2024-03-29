package com.kirinalex.BankBackOffice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CurrencyRateException extends Exception{

    HttpStatus status;

    public CurrencyRateException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
