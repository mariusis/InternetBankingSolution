package com.InternetBanking.InternetBanking.services;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.Deposit;
import com.InternetBanking.InternetBanking.repositories.AccountRepository;
import com.InternetBanking.InternetBanking.repositories.DepositRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import javax.transaction.Transactional;

@Service
public class DepositServiceImpl implements DepositService{
    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final DepositRepository depositRepository;

    public DepositServiceImpl(AccountRepository accountRepository, DepositRepository depositRepository) {
        this.accountRepository = accountRepository;

        this.depositRepository = depositRepository;
    }
    @Transactional
    public void deposit(String accountNumber, Double amount) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        }

        account.deposit(amount);
        accountRepository.save(account);

        Deposit deposit = new Deposit(account.getAccountNumber(),account.getAccountName(),amount, account.getCurrency());

        depositRepository.save(deposit);
    }
}
