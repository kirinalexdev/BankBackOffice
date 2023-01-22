package com.kirinalex.BankBackOffice.services;

import com.kirinalex.BankBackOffice.models.CardOrder;
import com.kirinalex.BankBackOffice.models.User;
import com.kirinalex.BankBackOffice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@AllArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
