package com.kirinalex.BankBackOffice.utils;

import lombok.Getter;

@Getter
public class BadRequestException extends Exception{

    public BadRequestException(String message) {
        super(message);
    }
}
