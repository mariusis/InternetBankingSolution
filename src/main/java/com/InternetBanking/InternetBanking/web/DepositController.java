package com.InternetBanking.InternetBanking.web;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.services.AccountService;
import com.InternetBanking.InternetBanking.services.DepositService;
import com.InternetBanking.InternetBanking.web.dto.DepositDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/deposit")
public class DepositController {

    @Autowired
    private final DepositService depositService; // Service for deposit-related logic
    private final AccountService accountService; // Service for account-related logic

    // Constructor Dependency Injection (DI)
    public DepositController(DepositService depositService, AccountService accountService) {
        this.depositService = depositService; // Initializing DepositService via DI
        this.accountService = accountService; // Initializing AccountService via DI
    }

    // Handles the route for displaying the deposit form
    @GetMapping
    public String showDepositForm(Model model, @AuthenticationPrincipal User currentUser) {
        // Fetch the list of accounts associated with the current user via AccountService
        List<Account> accounts = accountService.findAccountsOfCurrentUser(currentUser.getUsername());

        // Add an empty DepositDTO to the model for rendering the deposit form
        model.addAttribute("deposit", new DepositDTO());

        // Add the list of accounts to the model for display in the view
        model.addAttribute("accounts", accounts);

        // Render the 'deposit' HTML template to display the deposit form
        return "deposit";
    }

    // Handles the deposit submission
    @PostMapping
    public String deposit(@ModelAttribute("deposit") DepositDTO depositDTO, Model model) throws AccountNotFoundException {
        try {
            // Attempt to perform the deposit operation using DepositService
            depositService.deposit(depositDTO.getAccountNumber(), depositDTO.getAmount());

            // Inform the view that the deposit was successful
            model.addAttribute("success", true);

            // Redirect the user back to the deposit page with a success query parameter
            return "redirect:/deposit?success";
        } catch (IllegalArgumentException e) {
            // If an exception occurs (e.g., invalid inputs), handle the error
            model.addAttribute("error", e.getMessage());

            // Redirect the user back to the deposit page with an error query parameter
            return "redirect:/deposit?error=" + e.getMessage();
        }
    }
}