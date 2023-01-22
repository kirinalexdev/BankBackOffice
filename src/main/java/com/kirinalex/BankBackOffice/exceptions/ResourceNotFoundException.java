package com.kirinalex.BankBackOffice.exceptions;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends Exception{

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
