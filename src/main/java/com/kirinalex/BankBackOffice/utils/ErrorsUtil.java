package com.kirinalex.BankBackOffice.utils;

import org.springframework.validation.FieldError;
import java.util.List;

public class ErrorsUtil {

    public static String generateErrorMessage(List<FieldError> errors){
        var errorsSB = new StringBuilder();

        for (FieldError error: errors) {
            errorsSB.append(error.getField());
            errorsSB.append(" - ");
            errorsSB.append(error.getDefaultMessage());
            errorsSB.append("; ");
        }
        return errorsSB.toString();
    }

}
