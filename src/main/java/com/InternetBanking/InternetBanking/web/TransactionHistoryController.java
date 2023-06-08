package com.InternetBanking.InternetBanking.web;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.TransactionInfo;
import com.InternetBanking.InternetBanking.domain.Transfer;
import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.AccountRepository;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import com.InternetBanking.InternetBanking.services.TransactionHistoryService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/transaction-history")
public class TransactionHistoryController {
        private final TransactionHistoryService transactionHistoryService;
        private final AccountRepository accountRepository;
        private final UserRepository userRepository;
    public TransactionHistoryController(TransactionHistoryService transactionHistoryService, AccountRepository accountRepository, UserRepository userRepository) {
        this.transactionHistoryService = transactionHistoryService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }


    @GetMapping
    public  String transactionHistory(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username);
        List<TransactionInfo> transactions = transactionHistoryService.findAllByUser(username);
        List<Account> accounts = accountRepository.findAllByOwnerId(user.getUserId());
        model.addAttribute("accounts",accounts);
        model.addAttribute("history",transactions);
        return "transaction-history";
    }

}
