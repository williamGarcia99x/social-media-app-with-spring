package com.example.service;

import com.example.entity.Account;
import com.example.exception.DuplicateResourceException;
import com.example.exception.InvalidRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Registers a new account after performing validation.
     *
     * @param account The account object containing the username and password.
     * @return The registered account after being saved to the database.
     * @throws InvalidRequestException If the username is blank or the password is too short.
     * @throws DuplicateResourceException If an account with the same username already exists.
     */
    public Account register(Account account) throws InvalidRequestException, DuplicateResourceException {

        // Check if an account with the provided username already exists
        boolean accountExists = accountRepository.findByUsername(account.getUsername()).isPresent();

        // Validate that the username is not empty
        if (account.getUsername().isEmpty()) {
            throw new InvalidRequestException("Username cannot be blank.");
        }

        // Validate that the password meets the minimum length requirement
        if (account.getPassword().length() < 4) {
            throw new InvalidRequestException("Password has to be at least 4 characters long.");
        }

        // Check if the username is already taken
        if (accountExists) {
            throw new DuplicateResourceException("A user with this username already exists.");
        }

        // Persist the validated account in the database
        return accountRepository.save(account);
    }

    /**
     * Authenticates a user by checking the provided credentials.
     *
     * @param account The account object containing the username and password.
     * @return The authenticated account if credentials match.
     * @throws ResourceNotFoundException If no account with the given credentials is found.
     */
    public Account login(Account account) throws ResourceNotFoundException {
        return accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword())
                .orElseThrow(() -> new ResourceNotFoundException("No account was found with given credentials."));
    }

    /**
     * Retrieves an account by its unique ID.
     *
     * @param id The ID of the account to retrieve.
     * @return The account with the specified ID.
     * @throws ResourceNotFoundException If no account with the given ID is found.
     */
    public Account getUserById(int id) throws ResourceNotFoundException {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with this ID does not exist."));
    }
}















