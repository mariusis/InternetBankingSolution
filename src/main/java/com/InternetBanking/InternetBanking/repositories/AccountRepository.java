package com.InternetBanking.InternetBanking.repositories;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account,Long> {

    Account findByAccountId(Long accountId);
    Account findByAccountNumber(String accountNumber);
    Account findByAccountTypeAndOwnerId(String accountType, Long ownerId);
    List<Account> findAllByOwnerId(Long ownerId);


}
