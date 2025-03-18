package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {


    /**
     * @param username lookup account by the username
     * @return An Optional containing the account if it's found based on the username. An empty Optional otherwise
     */
    Optional<Account> findByUsername(String username);


    /**
     * @param username
     * @param password
     * @return An Optional containing the account if it's found based on the given credentials. An empty Optional
     * otherwise
     */
    Optional<Account> findByUsernameAndPassword(String username, String password);
}
