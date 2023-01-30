package com.kirinalex.BankBackOffice.utils;

import com.kirinalex.BankBackOffice.models.Authority;
import com.kirinalex.BankBackOffice.models.User;
import com.kirinalex.BankBackOffice.repositories.AuthorityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class AuthorityValidator implements Validator {

    private final AuthorityRepository authorityRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var name = ((Authority) target).getName();
        var optionalAuthority = authorityRepository.findByName(name);

        if (optionalAuthority.isPresent()) {
            errors.rejectValue("name", "", "Authority " + name + " уже существует");
        }
    }
}
