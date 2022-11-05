package com.kirinalex.BankBackOffice.utils;

import lombok.Getter;

@Getter
public class CurrencyRateException extends Exception{
    int statusCode;

    public CurrencyRateException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
