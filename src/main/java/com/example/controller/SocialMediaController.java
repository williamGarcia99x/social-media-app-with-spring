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


@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    // Constructor-based dependency injection for AccountService and MessageService
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * Handles user registration.
     * @param account The account information to be registered.
     * @return A ResponseEntity with the registered account and HTTP status 200 (OK).
     */
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.register(account));
    }

    /**
     * Handles user login.
     * @param account The account information (username and password).
     * @return A ResponseEntity with the logged-in account and HTTP status 200 (OK).
     */
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account loggedInAccount = accountService.login(account);
        return ResponseEntity.status(HttpStatus.OK).body(loggedInAccount);
    }

    /**
     * Creates a new message.
     * @param message The message object to be created.
     * @return A ResponseEntity with the created message and HTTP status 200 (OK).
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body(createdMessage);
    }

    /**
     * Retrieves all messages.
     * @return A ResponseEntity containing a list of all messages and HTTP status 200 (OK).
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages() {
        List<Message> messages = messageService.getMessages();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    /**
     * Retrieves a message by its ID.
     * @param messageId The ID of the message to be retrieved.
     * @return A ResponseEntity containing the found message or null if not found.
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message messageToFind = null;
        try {
            // Try to fetch the message, if not found, catch the exception
            messageToFind = messageService.getMessageById(messageId);
        } catch (ResourceNotFoundException ignored) {
            //This is to avoid the @ExceptionHandler from catching this exception. Instead, we want to execute the
            //return statement below
        }
        return ResponseEntity.status(HttpStatus.OK).body(messageToFind);
    }

    /**
     * Deletes a message by its ID.
     * @param messageId The ID of the message to be deleted.
     * @return A ResponseEntity with 1 if deletion was successful, otherwise null.
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
        Integer numberOfMessagesDeleted = messageService.deleteMessage(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(numberOfMessagesDeleted == 1 ? numberOfMessagesDeleted : null);
    }

    /**
     * Updates (patches) a message partially.
     * @param messageId The ID of the message to be patched.
     * @param message The message object containing the updates.
     * @return A ResponseEntity with 1 if the update was successful and HTTP status 200 (OK).
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> patchMessage(@PathVariable Integer messageId, @RequestBody Message message) {
        // If the patchMessage method is unsuccessful, it will throw an InvalidRequestException
        messageService.patchMessage(messageId, message);
        return ResponseEntity.status(HttpStatus.OK).body(1);
    }

    /**
     * Retrieves all messages associated with a specific account.
     * @param accountId The ID of the account whose messages are to be retrieved.
     * @return A ResponseEntity containing a list of messages belonging to the account.
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesFromAccount(@PathVariable Integer accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessagesByAccountId(accountId));
    }

    // ==============================
    // Exception Handlers
    // ==============================

    /**
     * Handles DuplicateResourceException.
     * Occurs when trying to create a resource that already exists.
     * @param exception The exception thrown.
     * @return A ResponseEntity with HTTP status 409 (CONFLICT) and the error message.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicateResourceException(DuplicateResourceException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    /**
     * Handles InvalidRequestException.
     * Occurs when a request contains invalid data or is improperly formatted.
     * @param exception The exception thrown.
     * @return A ResponseEntity with HTTP status 400 (BAD REQUEST) and the error message.
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    /**
     * Handles ResourceNotFoundException.
     * Occurs when a requested resource is not found.
     * @param exception The exception thrown.
     * @return A ResponseEntity with HTTP status 401 (UNAUTHORIZED) and the error message.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }
}
