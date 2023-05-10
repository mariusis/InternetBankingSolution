package com.InternetBanking.InternetBanking.services;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.AccountRepository;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import com.InternetBanking.InternetBanking.web.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {


    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Account save(AccountDto accountDto) {
        User user = userRepository.findByEmail(accountDto.getEmail());
        if (user == null) {
            // handle the case where the user doesn't exist
            return null;
        }

        Account acc = new Account(accountDto.getAccountType(), user.getUserId());

        accountRepository.save(acc); // save the account and update the id
        return acc;
    }
    @Override
    public List<Account> findAccountsByOwnerId(Long ownerId) {
        return accountRepository.findAllByOwnerId(ownerId);
    }
}