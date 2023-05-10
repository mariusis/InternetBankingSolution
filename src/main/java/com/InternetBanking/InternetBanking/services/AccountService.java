package com.InternetBanking.InternetBanking.services;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.web.dto.AccountDto;

import java.security.Principal;

public interface AccountService {
    Account save(AccountDto account);
}