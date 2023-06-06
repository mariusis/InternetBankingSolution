package com.InternetBanking.InternetBanking.repositories;

import com.InternetBanking.InternetBanking.domain.Deposit;
import com.InternetBanking.InternetBanking.repositories.DepositRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringJUnitConfig
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepositRepositoryTest {

    @Autowired
    private DepositRepository depositRepository;

    @Test
    public void testFindAllByAccountNumberIn() {
        // Create some test deposits
        Deposit deposit1 = new Deposit("123456", "Account1", 100.00,"$");
        Deposit deposit2 = new Deposit("789012", "Account2", 200.00,"$");
        Deposit deposit3 = new Deposit("345678", "Account3", 300.00,"$");

        // Save the deposits
        depositRepository.save(deposit1);
        depositRepository.save(deposit2);
        depositRepository.save(deposit3);

        // Create a list of account numbers to search for
        List<String> accountNumbers = Arrays.asList("123456", "345678");

        // Perform the repository query
        List<Deposit> deposits = depositRepository.findAllByAccountNumberIn(accountNumbers);

        // Verify the results
        assertEquals(2, deposits.size());
//        assertEquals("123456", deposits.get(0).getAccountNumber());
//        assertEquals("Account1", deposits.get(0).getAccountName());
//        assertEquals(100.00, deposits.get(0).getAmount());
//        assertEquals("345678", deposits.get(1).getAccountNumber());
//        assertEquals("Account3", deposits.get(1).getAccountName());
//        assertEquals(300.00, deposits.get(1).getAmount());
    }
}