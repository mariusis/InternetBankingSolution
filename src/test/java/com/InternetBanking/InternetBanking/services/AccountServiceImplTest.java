package com.InternetBanking.InternetBanking.services;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.AccountRepository;
import com.InternetBanking.InternetBanking.repositories.TransferRepository;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import com.InternetBanking.InternetBanking.services.AccountServiceImpl;
import com.InternetBanking.InternetBanking.web.dto.AccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransferRepository transferRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountServiceImpl(accountRepository, userRepository, transferRepository);
    }

    @Test
    void save_shouldCreateNewAccount() {
        AccountDto accountDto = new AccountDto();
        accountDto.setEmail("test@example.com");
        accountDto.setAccountType("savings");
        accountDto.setAccountName("My Savings Account");

        when(userRepository.findByEmail("test@example.com")).thenReturn(new User());
        when(accountRepository.save(any(Account.class))).thenReturn(new Account());

        Account savedAccount = accountService.save(accountDto);

        assertNotNull(savedAccount);
        assertEquals(accountDto.getAccountType(), savedAccount.getAccountType());
        assertEquals(accountDto.getAccountName(), savedAccount.getAccountName());
    }

    @Test
    void findAccountsOfCurrentUser_shouldReturnListOfAccounts() {
        User user = new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");

        Account account = new Account();
        account.setAccountId(1L);
        account.setAccountType("savings");
        account.setOwnerId(user.getUserId());
        account.setAccountName("My Savings Account");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(accountRepository.findAllByOwnerId(user.getUserId())).thenReturn(Collections.singletonList(account));

        List<Account> accounts = accountService.findAccountsOfCurrentUser("test@example.com");

        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        assertEquals(account.getAccountType(), accounts.get(0).getAccountType());
        assertEquals(account.getAccountName(), accounts.get(0).getAccountName());
    }

}