package com.kirinalex.BankBackOffice.services;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.kirinalex.BankBackOffice.models.Authority;
import com.kirinalex.BankBackOffice.models.User;
import com.kirinalex.BankBackOffice.repositories.AuthorityRepository;
import com.kirinalex.BankBackOffice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@AllArgsConstructor
@Transactional
public class AuthorityService {
    private final AuthorityRepository authorityRepositoryy;

    public void save(Authority authority){
        authorityRepositoryy.save(authority);
    }

    public Optional<Authority> findById(int id) {
        return authorityRepositoryy.findById(id);
    }

    public void delete(int id) {
        authorityRepositoryy.deleteById(id);
    }
}
