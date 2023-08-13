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

    // Constructor Dependency Injection (DI)
    public TransactionHistoryController(TransactionHistoryService transactionHistoryService, AccountRepository accountRepository, UserRepository userRepository) {
        this.transactionHistoryService = transactionHistoryService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    // Handles the route for displaying the transaction history
    @GetMapping
    public String transactionHistory(Model model) {
        // Get the username of the currently authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch the User instance using the UserRepository
        User user = userRepository.findByEmail(username);

        // Retrieve the list of TransactionInfo instances associated with the user
        List<TransactionInfo> transactions = transactionHistoryService.findAllByUser(username);

        // Fetch the list of accounts associated with the current user via AccountRepository
        List<Account> accounts = accountRepository.findAllByOwnerId(user.getUserId());

        // Add the list of accounts to the model for display in the view
        model.addAttribute("accounts", accounts);

        // Add the list of transactions to the model for display in the view
        model.addAttribute("history", transactions);

        // Render the 'transaction-history' HTML template to display the transaction history
        return "transaction-history";
    }
}
