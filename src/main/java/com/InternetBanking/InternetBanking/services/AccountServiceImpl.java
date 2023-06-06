package com.InternetBanking.InternetBanking.services;
import javax.transaction.Transactional;
import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.Transfer;
import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.AccountRepository;

import com.InternetBanking.InternetBanking.repositories.TransferRepository;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import com.InternetBanking.InternetBanking.web.dto.AccountDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {


    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransferRepository transferRepository;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transferRepository = transferRepository;

    }

    @Override
    public Account save(AccountDto accountDto) {
        User user = userRepository.findByEmail(accountDto.getEmail());
        if (user == null) {
            // handle the case where the user doesn't exist
            return null;
        }
        Account acc = new Account(accountDto.getAccountType(), user.getUserId(),accountDto.getAccountName(),accountDto.getCurrency());

        accountRepository.save(acc); // save the account and update the id
        return acc;
    }
    @Override
    public List<Account> findAccountsOfCurrentUser(String email) {
        User user = userRepository.findByEmail(email);
        return accountRepository.findAllByOwnerId(user.getUserId());
    }


}