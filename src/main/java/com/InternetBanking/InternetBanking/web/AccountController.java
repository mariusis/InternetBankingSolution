package com.InternetBanking.InternetBanking.web;

import com.InternetBanking.InternetBanking.domain.Account;

import com.InternetBanking.InternetBanking.repositories.UserRepository;
import com.InternetBanking.InternetBanking.services.AccountService;
import com.InternetBanking.InternetBanking.web.dto.AccountDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AccountController {

    private final AccountService accountService; // Our service to handle account-related logic
    private final UserRepository userRepository; // Repository for accessing user data

    // Constructor Dependency Injection (DI)
    public AccountController(AccountService accountService, UserRepository userRepository) {
        this.accountService = accountService; // Initializing our AccountService via DI
        this.userRepository = userRepository; // Initializing our UserRepository via DI
    }

    // Handles the route to show user's accounts
    @GetMapping("/accounts")
    public String showAccounts(Model model, @AuthenticationPrincipal User currentUser) {
        // Retrieve the instance of the current User from the UserRepository
        com.InternetBanking.InternetBanking.domain.User user = userRepository.findByEmail(currentUser.getUsername());

        // Fetch the list of Accounts associated with the current User via AccountService
        List<Account> accounts = accountService.findAccountsOfCurrentUser(currentUser.getUsername());

        // Add the list of Accounts to the model for display in the view
        model.addAttribute("accounts", accounts);

        // Render the 'accounts' HTML template to display the accounts
        return "accounts";
    }

    // Handles the route to show the account creation form
    @GetMapping("/accounts/create-account")
    public String showAccountCreationForm(Model model) {
        // Add an empty AccountDto to the model for rendering the account creation form
        model.addAttribute("account", new AccountDto());

        // Render the 'create-account' HTML template for creating a new account
        return "create-account";
    }

    // Handles the creation of a new banking account
    @PostMapping("/create-account")
    public String createBankingAccount(@ModelAttribute("account") AccountDto accountDto) {
        // Get the currently authenticated user's email from the authentication context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Set the email in the accountDto, as the account is associated with the current user
        accountDto.setEmail(email);

        // Save the accountDto using the AccountService to create a new banking account
        accountService.save(accountDto);

        // Redirect the user to the accounts page after successful account creation
        return "redirect:/accounts";
    }
}