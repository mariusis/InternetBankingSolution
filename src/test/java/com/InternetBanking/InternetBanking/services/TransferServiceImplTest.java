package com.InternetBanking.InternetBanking.services;

import static org.mockito.Mockito.*;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.Transfer;
import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.AccountRepository;
import com.InternetBanking.InternetBanking.repositories.TransferRepository;
import com.InternetBanking.InternetBanking.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransferServiceImpl transferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testTransferFunds() {
        // Given
        String senderAccountNumber = "123456";
        String recipientAccountNumber = "789012";
        Double amount = 100.0;
        String description = "deposit";
        Account senderAccount = new Account();
        senderAccount.setAccountNumber(senderAccountNumber);
        senderAccount.setOwnerId(1L);
        senderAccount.setBalance(150.0); // Set the sender's account balance

        Account recipientAccount = new Account();
        recipientAccount.setAccountNumber(recipientAccountNumber);
        recipientAccount.setOwnerId(2L);

        Transfer transfer = new Transfer(senderAccount, recipientAccount, amount, "Sender Name", "Recipient Name", "Rent");

        when(accountRepository.findByAccountNumber(senderAccountNumber)).thenReturn(senderAccount);
        when(accountRepository.findByAccountNumber(recipientAccountNumber)).thenReturn(recipientAccount);
        when(userRepository.findByUserId(senderAccount.getOwnerId())).thenReturn(new User());
        when(userRepository.findByUserId(recipientAccount.getOwnerId())).thenReturn(new User());

        // Mock the authentication in SecurityContextHolder
        Authentication authentication = new UsernamePasswordAuthenticationToken("sender", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When
        transferService.transferFunds(senderAccountNumber, recipientAccountNumber, amount,description);

        // Then
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transferRepository, times(1)).save(any(Transfer.class));
        assertEquals(senderAccount.getBalance(), 50.0); // Expected sender's balance after transfer
        assertEquals(recipientAccount.getBalance(), 100.0); // Expected recipient's balance after transfer
    }
}