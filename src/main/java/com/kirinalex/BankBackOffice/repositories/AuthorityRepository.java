package com.kirinalex.BankBackOffice.repositories;

import com.kirinalex.BankBackOffice.models.Authority;
import com.kirinalex.BankBackOffice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    Optional<Authority> findByName(String name);

}
