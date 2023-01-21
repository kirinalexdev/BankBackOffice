package com.kirinalex.BankBackOffice.services;

import com.kirinalex.BankBackOffice.repositories.UserRepository;
import com.kirinalex.BankBackOffice.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException("Пользователь " + username + " не найден");

        return new UserDetailsImpl(optionalUser.get());
    }
}
