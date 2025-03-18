package com.example.service;

import com.example.entity.Account;
import com.example.exception.DuplicateResourceException;
import com.example.exception.InvalidRequestException;
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
        /*
        * To successfully register a user:
        * 1. username is not blank
        * 2. password is at least 4 characters long
        * 3. Account username is unique
        *
        * */
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











}
