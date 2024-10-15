package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;
   
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }


    @PostMapping(value="/register")
    public ResponseEntity registerAccount(@RequestBody Account account){
        try {
            return ResponseEntity.ok(accountService.registerAccount(account));
        } catch (DuplicateUsernameException e) {
            return ResponseEntity.status(409).body("Username already exists!");
        } catch(Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody Account account){
        try {
            Account response = accountService.login(account);
            if (response==null) {
                return ResponseEntity.status(401).body("Unauthorized!");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized!");
        }
    }
    @PostMapping(value = "/messages")
    public ResponseEntity postMessage(@RequestBody Message message){
        try {
            return ResponseEntity.ok(messageService.addMessage(message));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    @GetMapping(value = "/messages")
    public List<Message> getMessages(){
        return messageService.getAllMessages();
    }

    @GetMapping(value = "/messages/{messageid}")
    public Message getMessageById(@PathVariable int messageid){
        return messageService.getMessageById(messageid);
    }
    @DeleteMapping(value = "/messages/{messageid}")
    public Integer deleteMessageById(@PathVariable int messageid){
        return messageService.deleteMessageById(messageid);
    }

    @PatchMapping(value = "/messages/{messageid}")
    public ResponseEntity deleteMessageById(@RequestBody Message message, @PathVariable int messageid){
        try {
            return ResponseEntity.ok(messageService.updateMessageTextById(messageid,message.getMessageText()));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
        
    }

    @GetMapping(value = "/accounts/{accountId}/messages")
    public List<Message> getMessagesByUser(@PathVariable int accountId){
        return messageService.getMessagesByUser(accountId);
    }

}
