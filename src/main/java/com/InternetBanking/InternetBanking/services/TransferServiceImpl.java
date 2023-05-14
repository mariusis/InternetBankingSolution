package com.InternetBanking.InternetBanking.services;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.Transfer;
import com.InternetBanking.InternetBanking.repositories.AccountRepository;
import com.InternetBanking.InternetBanking.repositories.TransferRepository;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
@Service
public class TransferServiceImpl implements TransferService {
    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final TransferRepository transferRepository;
    @Autowired
    private final UserRepository userRepository;

    public TransferServiceImpl(AccountRepository accountRepository, TransferRepository transferRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void transferFunds(String senderAccountNumber, String recipientAccountNumber, Double amount) {
        String senderUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Account senderAccount = accountRepository.findByAccountNumber(senderAccountNumber);
        Account recipientAccount = accountRepository.findByAccountNumber(recipientAccountNumber);

        senderAccount.transfer(recipientAccount, amount);
        Transfer transfer = new Transfer(senderAccount, recipientAccount, amount,userRepository.findByUserId(senderAccount.getOwnerId()).getFullName(),userRepository.findByUserId(recipientAccount.getOwnerId()).getFullName());
        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);
        transferRepository.save(transfer);
    }
}
