package com.InternetBanking.InternetBanking.web;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.services.AccountService;
import com.InternetBanking.InternetBanking.services.TransferService;
import com.InternetBanking.InternetBanking.web.dto.TransferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private final TransferService transferService; // Service for transfer-related logic
    @Autowired
    private final AccountService accountService; // Service for account-related logic

    // Constructor Dependency Injection (DI)
    public TransactionController(TransferService transferService, AccountService accountService) {
        this.transferService = transferService; // Initializing TransferService via DI
        this.accountService = accountService; // Initializing AccountService via DI
    }

    // Handles the route for displaying the transfer form
    @GetMapping
    public String showTransferForm(Model model, @AuthenticationPrincipal User currentUser) {
        // Fetch the list of accounts associated with the current user via AccountService
        List<Account> accounts = accountService.findAccountsOfCurrentUser(currentUser.getUsername());

        // Add an empty TransferDTO to the model for rendering the transfer form
        model.addAttribute("transaction", new TransferDTO());

        // Add the list of accounts to the model for display in the view
        model.addAttribute("accounts", accounts);

        // Render the 'transactional' HTML template to display the transfer form
        return "transaction";
    }

    // Handles the transfer submission
    @PostMapping
    public String transferFunds(@ModelAttribute("transaction") TransferDTO transferDTO, Model model) {
        try {
            // Attempt to transfer funds using the TransferService
            transferService.transferFunds(
                    transferDTO.getSenderAccountNumber(),
                    transferDTO.getRecipientAccountNumber(),
                    transferDTO.getAmount(),
                    transferDTO.getDescription());

            // Inform the view that the transfer was successful
            model.addAttribute("success", true);

            // Redirect the user back to the transaction page with a success query parameter
            return "redirect:/transaction?success";
        } catch (IllegalArgumentException e) {
            // If an exception occurs (e.g., insufficient funds), handle the error
            model.addAttribute("error", e.getMessage());

            // Redirect the user back to the transaction page with an error query parameter
            return "redirect:/transaction?error=" + e.getMessage();
        }
    }
}