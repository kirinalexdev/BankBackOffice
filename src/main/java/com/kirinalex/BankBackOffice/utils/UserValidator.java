package com.kirinalex.BankBackOffice.utils;

import com.kirinalex.BankBackOffice.models.User;
import com.kirinalex.BankBackOffice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var user = (User) target; // todo лишняя переменная
        var username = user.getUsername();
        var optionalUser = userRepository.findByUsername(username);

        // todo педелелать на isPresent
        if (!optionalUser.isEmpty()) {
            errors.rejectValue("username", "", "Пользователь "+ username +" уже существует");
        }

    }
}
