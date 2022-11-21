package com.kirinalex.BankBackOffice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirinalex.BankBackOffice.dto.EmployeeDTO;
import org.slf4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static javax.sql.rowset.spi.SyncFactory.getLogger;

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
