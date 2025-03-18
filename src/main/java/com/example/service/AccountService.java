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


    public Account register(Account account) throws InvalidRequestException, DuplicateResourceException{

        //perform validation first
        boolean accountExists = accountRepository.findByUsername(account.getUsername()).isPresent();

        if(account.getUsername().isEmpty()){
            throw new InvalidRequestException("Username cannot be blank");
        }
        if(account.getPassword().length() < 4){
            throw new InvalidRequestException("Password has to be at least 4 characters long");
        }
        if(accountExists){
            throw new DuplicateResourceException("A user with this username already exists");
        }


        //Now we can persist the account into the database
        return accountRepository.save(account);
    }

    public Account login(Account account) throws ResourceNotFoundException{
        return accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword()).
                orElseThrow(() -> new ResourceNotFoundException("No account was found with given credentials"));
    }













}
