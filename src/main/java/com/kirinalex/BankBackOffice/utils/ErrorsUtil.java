package com.kirinalex.BankBackOffice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kirinalex.BankBackOffice.dto.EmployeeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
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

    public static void checkBindingResult(BindingResult bindingResult, Object object) throws BadRequestException, JsonProcessingException {
        if (bindingResult.hasErrors()) {
            var message = generateErrorMessage(bindingResult.getFieldErrors());
            log.error("{}. {}", message, object, new Throwable());
            throw new BadRequestException(message);
        }
    }

//    public static ResponseEntity<Object> errorResponseNotFound(String message, HttpServletRequest httpRequest){
//        var status = HttpStatus.NOT_FOUND;
//        var error =  new ErrorResponse(status, message, httpRequest);
//        return ResponseEntity
//                .status(status)
//                .body(error);
//    }
}
