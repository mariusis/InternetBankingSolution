package com.InternetBanking.InternetBanking.repositories;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setup() {
        // Set up some test data
        User user1 = new User("John", "Doe", "johndoe@example.com", "password", null);
        User user2 = new User("Jane", "Smith", "janesmith@example.com", "password", null);

        entityManager.persist(user1);
        entityManager.persist(user2);

        Account account1 = new Account("Savings", user1.getUserId(), "Account 1","dolar");
        Account account2 = new Account("Checking", user1.getUserId(), "Account 2","dolar");
        Account account3 = new Account("Savings", user2.getUserId(), "Account 3","dolar");

        entityManager.persist(account1);
        entityManager.persist(account2);
        entityManager.persist(account3);
        entityManager.flush();
    }

    @Test
    public void testFindByAccountId() {
        // Given
        Long accountId = 1L;

        // When
        Account account = accountRepository.findByAccountId(accountId);

        // Then
        Assertions.assertNotNull(account);
        Assertions.assertEquals(accountId, account.getAccountId());
    }

    @Test
    public void testFindByAccountNumber() {
        // Given
        String accountNumber = "RO40123456789";

        // When
        Account account = accountRepository.findByAccountNumber(accountNumber);

        // Then
        Assertions.assertNotNull(account);
        Assertions.assertEquals(accountNumber, account.getAccountNumber());
    }

    @Test
    public void testFindByAccountTypeAndOwnerId() {
        // Given
        String accountType = "Savings";
        Long ownerId = 2L;

        // When
        Account account = accountRepository.findByAccountTypeAndOwnerId(accountType, ownerId);

        // Then
        Assertions.assertNotNull(account);
        Assertions.assertEquals(accountType, account.getAccountType());
        Assertions.assertEquals(ownerId, account.getOwnerId());
    }

    @Test
    public void testFindAllByOwnerId() {
        // Given
        Long ownerId = 1L;

        // When
        List<Account> accounts = accountRepository.findAllByOwnerId(ownerId);

        // Then
        Assertions.assertNotNull(accounts);
        Assertions.assertEquals(2, accounts.size());
        Assertions.assertTrue(accounts.stream().allMatch(a -> a.getOwnerId().equals(ownerId)));
    }
}