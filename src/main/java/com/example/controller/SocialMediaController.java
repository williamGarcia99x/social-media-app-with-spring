package com.example.controller;


import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateResourceException;
import com.example.exception.InvalidRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account){
        return ResponseEntity.status(HttpStatus.OK).body(accountService.register(account));
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        Account loggedInAccount = accountService.login(account);
        return ResponseEntity.status(HttpStatus.OK).body(loggedInAccount);
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body(createdMessage);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages(){
        List<Message> messages = messageService.getMessages();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId){

        Message messageToFind = null;
        try{
            messageToFind = messageService.getMessageById(messageId);
        } catch (ResourceNotFoundException ignored){
        }

        return ResponseEntity.status(HttpStatus.OK).body(messageToFind);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicateResourceException(DuplicateResourceException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }

















}
