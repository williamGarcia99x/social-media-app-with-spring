package com.example.service;

import com.example.entity.Message;
import com.example.exception.InvalidRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountService accountService;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountService accountService) {
        this.messageRepository = messageRepository;
        this.accountService = accountService;
    }

    /**
     * Creates a new message and saves it to the database after validating the request.
     *
     * @param message The message object containing the text and the user who posted it.
     * @return The created message after being saved to the database.
     * @throws InvalidRequestException If the message is invalid or the posting user does not exist.
     */
    public Message createMessage(Message message) throws InvalidRequestException {

        // Validate that the user posting the message exists
        try {
            accountService.getUserById(message.getPostedBy());
        } catch (ResourceNotFoundException e) {
            throw new InvalidRequestException("Message needs to be posted by a valid user.");
        }

        // Check if the message is empty
        if (message.getMessageText().isEmpty()) {
            throw new InvalidRequestException("Message cannot be blank.");
        }

        // Check if the message exceeds the 255-character limit
        if (message.getMessageText().length() > 255) {
            throw new InvalidRequestException("Message cannot be over 255 characters.");
        }

        // Save and return the valid message
        return messageRepository.save(message);
    }

    /**
     * Retrieves a message by its ID.
     *
     * @param messageId The ID of the message to retrieve.
     * @return The message if found.
     * @throws ResourceNotFoundException If no message with the specified ID exists.
     */
    public Message getMessageById(int messageId) throws ResourceNotFoundException {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message with this ID does not exist."));
    }

    /**
     * Retrieves all messages stored in the database.
     *
     * @return A list of all available messages.
     */
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    /**
     * Deletes a message by its ID if it exists.
     *
     * @param messageId The ID of the message to delete.
     * @return 1 if the message was successfully deleted, 0 otherwise.
     */
    public Integer deleteMessage(Integer messageId) {

        // Check if the message exists before deleting
        if (messageRepository.findById(messageId).isPresent()) {
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }

    /**
     * Updates an existing message with new content.
     *
     * @param messageId The ID of the message to update.
     * @param message The message containing updated content.
     * @throws InvalidRequestException If the message is invalid or if the message ID does not exist.
     */
    public void patchMessage(Integer messageId, Message message) throws InvalidRequestException {

        // Validate that the message to update exists
        Message messageToUpdate = messageRepository.findById(messageId)
                .orElseThrow(() -> new InvalidRequestException("Cannot update a message with this ID because it does not exist."));

        // Check if the new message text is empty
        if (message.getMessageText().isEmpty()) {
            throw new InvalidRequestException("Message cannot be blank.");
        }

        // Check if the new message text exceeds the 255-character limit
        if (message.getMessageText().length() > 255) {
            throw new InvalidRequestException("Message cannot be over 255 characters.");
        }

        // Update the message content and save the changes
        messageToUpdate.setMessageText(message.getMessageText());
        messageRepository.save(messageToUpdate);
    }

    /**
     * Retrieves all messages posted by a specific account.
     *
     * @param accountId The ID of the account whose messages should be retrieved.
     * @return A list of messages posted by the specified account.
     */
    public List<Message> getMessagesByAccountId(Integer accountId) {

        // Return the list of messages posted by the given account ID
        return (List<Message>) messageRepository.findByAccount_PostedBy(accountId);
    }
}

