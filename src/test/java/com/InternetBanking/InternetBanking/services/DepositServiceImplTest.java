package com.InternetBanking.InternetBanking.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.Deposit;
import com.InternetBanking.InternetBanking.repositories.AccountRepository;
import com.InternetBanking.InternetBanking.repositories.DepositRepository;

import javax.security.auth.login.AccountNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DepositServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private DepositRepository depositRepository;

    @InjectMocks
    private DepositServiceImpl depositService;

    @Test
    public void testDeposit() throws AccountNotFoundException {
        // Given
        String accountNumber = "1234567890";
        Double amount = 100.0;

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(500.0);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(account);

        // When
        depositService.deposit(accountNumber, amount);

        // Then
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
        verify(accountRepository, times(1)).save(account);

        Deposit expectedDeposit = new Deposit(account.getAccountNumber(), account.getAccountName(), amount, account.getCurrency());
        verify(depositRepository, times(1)).save(expectedDeposit);
    }

    @Test
    public void testDepositThrowsAccountNotFoundException() throws AccountNotFoundException {
        // Given
        String accountNumber = "1234567890";
        Double amount = 100.0;

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(null);

        // When and then
        assertThrows(AccountNotFoundException.class, () -> depositService.deposit(accountNumber, amount));

        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
        verify(accountRepository, never()).save(any(Account.class));
        verify(depositRepository, never()).save(any(Deposit.class));
    }
}