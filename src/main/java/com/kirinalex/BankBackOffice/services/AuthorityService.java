package com.kirinalex.BankBackOffice.services;

import com.kirinalex.BankBackOffice.models.Authority;
import com.kirinalex.BankBackOffice.repositories.AuthorityRepository;
import lombok.AllArgsConstructor;
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
