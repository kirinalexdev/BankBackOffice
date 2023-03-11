package com.kirinalex.BankBackOffice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kirinalex.BankBackOffice.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Slf4j
public class ErrorsUtil {

    public static String generateErrorMessage(List<FieldError> fieldErrors){
        var errors = new StringBuilder();

        fieldErrors.forEach(error -> {
            errors.append(error.getField());
            errors.append(" - ");
            errors.append(error.getDefaultMessage());
            errors.append("; ");
        });

        return errors.toString();
    }

    public static void checkBindingResult(BindingResult bindingResult, Object object) throws BadRequestException, JsonProcessingException {
        if (bindingResult.hasErrors()) {
            var message = generateErrorMessage(bindingResult.getFieldErrors());
            log.error("{}. {}", message, object, new Throwable());
            throw new BadRequestException(message);
        }
    }
}
