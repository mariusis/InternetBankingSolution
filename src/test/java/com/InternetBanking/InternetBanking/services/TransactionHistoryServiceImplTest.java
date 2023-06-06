package com.InternetBanking.InternetBanking.services;

import static org.mockito.Mockito.*;

import com.InternetBanking.InternetBanking.domain.*;
import com.InternetBanking.InternetBanking.repositories.AccountRepository;
import com.InternetBanking.InternetBanking.repositories.DepositRepository;
import com.InternetBanking.InternetBanking.repositories.TransferRepository;
import com.InternetBanking.InternetBanking.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionHistoryServiceImplTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private DepositRepository depositRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionHistoryServiceImpl transactionHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindAllByUser() {
        // Given
        String username = "test@example.com";

        User user = new User();
        user.setUserId(1L);
        user.setEmail(username);

        Account account1 = new Account();
        account1.setAccountId(1L);
        account1.setAccountNumber("123456");
        account1.setAccountName("Account 1");
        account1.setOwnerId(user.getUserId());

        Account account2 = new Account();
        account2.setAccountId(2L);
        account2.setAccountNumber("789012");
        account2.setAccountName("Account 2");
        account2.setOwnerId(user.getUserId());

        List<Account> accounts = Arrays.asList(account1, account2);
        List<Long> accountIds = Arrays.asList(account1.getAccountId(), account2.getAccountId());

        Transfer transfer1 = new Transfer();
        transfer1.setTransferId(1L);
        transfer1.setSender(account1);
        transfer1.setReceiver(account2);
        transfer1.setAmount(100.0);
        transfer1.setDate(LocalDateTime.now());

        Transfer transfer2 = new Transfer();
        transfer2.setTransferId(2L);
        transfer2.setSender(account2);
        transfer2.setReceiver(account1);
        transfer2.setAmount(200.0);
        transfer2.setDate(LocalDateTime.now().minusDays(1));

        List<Transfer> transfers = Arrays.asList(transfer1, transfer2);
        List<String> accountNumbers = Arrays.asList(account1.getAccountNumber(), account2.getAccountNumber());

        Deposit deposit1 = new Deposit();
        deposit1.setId(1L);
        deposit1.setAccountNumber(account1.getAccountNumber());
        deposit1.setAccountName(account1.getAccountName());
        deposit1.setAmount(500.0);
        deposit1.setDate(LocalDateTime.now().minusDays(2));

        Deposit deposit2 = new Deposit();
        deposit2.setId(2L);
        deposit2.setAccountNumber(account2.getAccountNumber());
        deposit2.setAccountName(account2.getAccountName());
        deposit2.setAmount(1000.0);
        deposit2.setDate(LocalDateTime.now().minusDays(3));

        List<Deposit> deposits = Arrays.asList(deposit1, deposit2);

        when(userRepository.findByEmail(username)).thenReturn(user);
        when(accountRepository.findAllByOwnerId(user.getUserId())).thenReturn(accounts);
        when(transferRepository.findAllBySenderAccountIdInOrReceiverAccountIdIn(accountIds, accountIds)).thenReturn(transfers);
        when(depositRepository.findAllByAccountNumberIn(accountNumbers)).thenReturn(deposits);

        List<TransactionInfo> expectedTransactionInfoList = new ArrayList<>();

        TransactionInfo depositTransactionInfo1 = new TransactionInfo();
        depositTransactionInfo1.setId(deposit1.getId());
        depositTransactionInfo1.setSource("ATM");
        depositTransactionInfo1.setDateTime(deposit1.getDate());
        depositTransactionInfo1.setAccountNumber(deposit1.getAccountNumber());
        depositTransactionInfo1.setAccountName(deposit1.getAccountName());
        depositTransactionInfo1.setAmount(deposit1.getAmount());
        depositTransactionInfo1.setTransactionType("DEPOSIT");
        expectedTransactionInfoList.add(depositTransactionInfo1);

        TransactionInfo depositTransactionInfo2 = new TransactionInfo();
        depositTransactionInfo2.setId(deposit2.getId());
        depositTransactionInfo2.setSource("ATM");
        depositTransactionInfo2.setDateTime(deposit2.getDate());
        depositTransactionInfo2.setAccountNumber(deposit2.getAccountNumber());
        depositTransactionInfo2.setAccountName(deposit2.getAccountName());
        depositTransactionInfo2.setAmount(deposit2.getAmount());
        depositTransactionInfo2.setTransactionType("DEPOSIT");
        expectedTransactionInfoList.add(depositTransactionInfo2);

        TransactionInfo transferTransactionInfo1 = new TransactionInfo();
        transferTransactionInfo1.setId(transfer1.getTransferId());
        transferTransactionInfo1.setSource(transfer1.getSender().getAccountName());
        transferTransactionInfo1.setDateTime(transfer1.getDate());
        transferTransactionInfo1.setAccountNumber(transfer1.getReceiverName());
        transferTransactionInfo1.setAccountName(transfer1.getReceiver().getAccountName());
        transferTransactionInfo1.setAmount(transfer1.getAmount());
        transferTransactionInfo1.setTransactionType("TRANSFER");
        expectedTransactionInfoList.add(transferTransactionInfo1);

        TransactionInfo transferTransactionInfo2 = new TransactionInfo();
        transferTransactionInfo2.setId(transfer2.getTransferId());
        transferTransactionInfo2.setSource(transfer2.getSender().getAccountName());
        transferTransactionInfo2.setDateTime(transfer2.getDate());
        transferTransactionInfo2.setAccountNumber(transfer2.getReceiverName());
        transferTransactionInfo2.setAccountName(transfer2.getReceiver().getAccountName());
        transferTransactionInfo2.setAmount(transfer2.getAmount());
        transferTransactionInfo2.setTransactionType("TRANSFER");
        expectedTransactionInfoList.add(transferTransactionInfo2);

        // When
        List<TransactionInfo> actualTransactionInfoList = transactionHistoryService.findAllByUser(username);

        // Then
        assertEquals(expectedTransactionInfoList.size(), actualTransactionInfoList.size());
        for (int i = 0; i < expectedTransactionInfoList.size(); i++) {
            TransactionInfo expectedTransactionInfo = expectedTransactionInfoList.get(i);
            TransactionInfo actualTransactionInfo = actualTransactionInfoList.get(i);
            assertEquals(expectedTransactionInfo.getId(), actualTransactionInfo.getId());
            assertEquals(expectedTransactionInfo.getSource(), actualTransactionInfo.getSource());
            assertEquals(expectedTransactionInfo.getDateTime(), actualTransactionInfo.getDateTime());
            assertEquals(expectedTransactionInfo.getAccountNumber(), actualTransactionInfo.getAccountNumber());
            assertEquals(expectedTransactionInfo.getAccountName(), actualTransactionInfo.getAccountName());
            assertEquals(expectedTransactionInfo.getAmount(), actualTransactionInfo.getAmount());
            assertEquals(expectedTransactionInfo.getTransactionType(), actualTransactionInfo.getTransactionType());
        }
    }
}