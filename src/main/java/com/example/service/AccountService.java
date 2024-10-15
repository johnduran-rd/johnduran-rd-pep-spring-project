package com.example.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.*;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository= accountRepository;
    }

    public Account registerAccount(Account account) throws DuplicateUsernameException, InvalidCredentialsException {
        if (ValidCredentials(account)) {          
            if (accountRepository.existsByUsername(account.getUsername())) {
                throw new DuplicateUsernameException();
            }
            return accountRepository.save(account);
        }
        throw new InvalidCredentialsException();
    }
    
    public Account login(Account account) throws InvalidCredentialsException{
        if (ValidCredentials(account)) {
            return accountRepository.findAccountByUsernameAndPassword(account.getUsername(), account.getPassword());       
        }
        throw new InvalidCredentialsException();
    }

    private boolean ValidCredentials(Account account){
        return !account.getUsername().isBlank() && account.getPassword().length()>3;
    }
  


}
