package com.InternetBanking.InternetBanking.services;

import com.InternetBanking.InternetBanking.domain.*;
import com.InternetBanking.InternetBanking.repositories.AccountRepository;

import com.InternetBanking.InternetBanking.repositories.DepositRepository;
import com.InternetBanking.InternetBanking.repositories.TransferRepository;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionHistoryServiceImpl implements TransactionHistoryService {
    private final TransferRepository transferRepository;
    private final DepositRepository depositRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public TransactionHistoryServiceImpl(TransferRepository transferRepository, DepositRepository depositRepository, UserRepository userRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.depositRepository = depositRepository;

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }
    public List<TransactionInfo> findAllByUser(String username){

        User user = userRepository.findByEmail(username);
        List<Account> accounts = accountRepository.findAllByOwnerId(user.getUserId());
        List<Long> accountIds = accounts.stream().map(Account::getAccountId).collect(Collectors.toList());
        List<Transfer> transfers = transferRepository.findAllBySenderAccountIdInOrReceiverAccountIdIn(accountIds,accountIds);
        List<String> accountNumbers = accounts.stream().map(Account::getAccountNumber).collect(Collectors.toList());
        List<Deposit> deposits = depositRepository.findAllByAccountNumberIn(accountNumbers);
        List<TransactionInfo> transactionInfoList = new ArrayList<>();

        
        for (Deposit deposit : deposits) {
            TransactionInfo transactionInfo = new TransactionInfo();
            transactionInfo.setId(deposit.getId());
            transactionInfo.setSource("ATM");
            transactionInfo.setDateTime(deposit.getDate());
            transactionInfo.setAccountNumber(deposit.getAccountNumber());
            transactionInfo.setAccountName(deposit.getAccountName());
            transactionInfo.setAmount(deposit.getAmount());
            transactionInfo.setTransactionType("DEPOSIT");
            transactionInfoList.add(transactionInfo);
        }
        
        for (Transfer transfer : transfers) {
            TransactionInfo transferInfo = new TransactionInfo();
            transferInfo.setId(transfer.getTransferId());
            transferInfo.setSource(transfer.getSender().getAccountName());
            transferInfo.setDateTime(transfer.getDate());
            transferInfo.setAccountNumber(transfer.getReceiverName());
            transferInfo.setAccountName(transfer.getReceiver().getAccountName());
            transferInfo.setAmount(transfer.getAmount());
            transferInfo.setTransactionType("TRANSFER");
            transactionInfoList.add(transferInfo);
        }
        Collections.sort(transactionInfoList, Comparator.comparing(TransactionInfo::getDateTime));
        
        return transactionInfoList;
    }
}
