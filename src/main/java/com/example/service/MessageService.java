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


    public Message createMessage(Message message) throws InvalidRequestException{

        //validation
        try {
            accountService.getUserById(message.getPostedBy());
        } catch (ResourceNotFoundException e){
            throw new InvalidRequestException("Message needs to be posted by a valid user.");
        }

        if(message.getMessageText().isEmpty()){
            throw new InvalidRequestException("Message cannot be blank.");
        }

        if(message.getMessageText().length() > 255){
            throw new InvalidRequestException("Message cannot be over 255 characters.");
        }

        return messageRepository.save(message);


    }

    public Message getMessageById(int messageId) throws ResourceNotFoundException{
        return messageRepository.findById(messageId).orElseThrow(() -> new ResourceNotFoundException("Message with this ID" +
                " does not exist"));
    }

    public List<Message> getMessages(){
        return messageRepository.findAll();
    }

    public Integer deleteMessage(Integer messageId){

        if(messageRepository.findById(messageId).isPresent()){
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;

    }

    public void patchMessage(Integer messageId, Message message) throws InvalidRequestException{

        //validation
        Message messageToUpdate = messageRepository.findById(messageId).orElseThrow(() -> new InvalidRequestException("Cannot " +
                "update a message with this ID because it does not exist."));


        if(message.getMessageText().isEmpty()){
            throw new InvalidRequestException("Message cannot be blank.");
        }

        if(message.getMessageText().length() > 255){
            throw new InvalidRequestException("Message cannot be over 255 characters.");
        }

        messageToUpdate.setMessageText(message.getMessageText());

        messageRepository.save(messageToUpdate);

    }

    public List<Message> getMessagesByAccountId(Integer accountId){

        return (List<Message>) messageRepository.findByAccount_PostedBy(accountId);
    }


}
