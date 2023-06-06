package com.InternetBanking.InternetBanking.repositories;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.Transfer;
import com.InternetBanking.InternetBanking.repositories.TransferRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringJUnitConfig
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransferRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransferRepository transferRepository;

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testFindAllBySenderAccountIdInOrReceiverAccountIdIn() {
        // Create test data
        Account sender = new Account();
        sender.setAccountId(1L);
        Account receiver = new Account();
        receiver.setAccountId(2L);
        Transfer transfer1 = new Transfer(sender, receiver, 100.0, "Sender1", "Receiver1","Rent");
        Transfer transfer2 = new Transfer(sender, receiver, 200.0, "Sender2", "Receiver2","Rent");
        entityManager.persist(transfer1);
        entityManager.persist(transfer2);
        entityManager.flush();

        // Perform the repository method
        List<Transfer> transfers = transferRepository.findAllBySenderAccountIdInOrReceiverAccountIdIn(
                Arrays.asList(sender.getAccountId()),
                Arrays.asList(receiver.getAccountId())
        );

        // Verify the results
        Assertions.assertEquals(2, transfers.size());
        Assertions.assertTrue(transfers.contains(transfer1));
        Assertions.assertTrue(transfers.contains(transfer2));
    }
}