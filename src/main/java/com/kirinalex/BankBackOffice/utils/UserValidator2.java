//package com.kirinalex.BankBackOffice.utils;
//
//import com.kirinalex.BankBackOffice.models.User;
//import com.kirinalex.BankBackOffice.repositories.UserRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//
//@Component
//@AllArgsConstructor
//public class UserValidator2 implements Validator {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public boolean supports(Class<?> clazz) {
//        return User.class.equals(clazz);
//    }
//
//    @Override
//    public void validate(Object target, Errors errors) {
//        var id = ((User) target).getId();
//        var optionalUser = userRepository.findById(id);
//
//        if (optionalUser.isEmpty()) {
//            errors.reject( "112", "Не найдено по id");
//            errors.rejectValue("id", "", "Не найдено по id");
//        }
//
////        var status = HttpStatus.NOT_FOUND;
////        var error =  new ErrorResponse(status, "Не найден пользователь с id = " + idUser, httpRequest);
////        return ResponseEntity
////                .status(status)
////                .body(error);
//
////        var optionalUser = userRepository.findByUsername(username);
////
////        if (!optionalUser.isEmpty()) {
////            errors.rejectValue("username", "", "Пользователь "+ username +" уже существует");
////        }
//
//    }
//}
