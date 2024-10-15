package com.example.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.InvalidMessageTextException;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message addMessage(Message message) throws InvalidMessageTextException{
        if (messageTextIsValid(message.getMessageText())) {
           return messageRepository.save(message);
        }
        throw new InvalidMessageTextException();
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(int id){
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get();
        }
        return null;
    }

    public Integer deleteMessageById(int id){
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return 1;
        }
        return null;        
    }
    public Integer updateMessageTextById(int messageId, String messageText) throws InvalidMessageTextException{
        if (messageTextIsValid(messageText)) {           
            Optional<Message> optionalMessage = messageRepository.findById(messageId);
            if (optionalMessage.isPresent()) {
                Message message = optionalMessage.get();
                message.setMessageText(messageText);
                messageRepository.save(message);
                return 1;
            }
            throw new EntityNotFoundException();
        }
        throw new InvalidMessageTextException();
    }

    public List<Message> getMessagesByUser(Integer accountId){
        return messageRepository.findMessagesByPostedBy(accountId);
    }

    private boolean messageTextIsValid(String message){
        if (message.length()>=255 || message.isBlank()) {
            return false;
        }
        return true;
    }

}
