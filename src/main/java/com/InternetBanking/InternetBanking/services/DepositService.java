package com.InternetBanking.InternetBanking.services;

import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

@Service
public interface DepositService {
    public void deposit(String accountNumber, Double amount) throws AccountNotFoundException;
}
