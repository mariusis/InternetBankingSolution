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
    private final TransferService transferService;
    @Autowired
    private final AccountService accountService;

    public TransactionController(TransferService transferService, AccountService accountService) {
        this.transferService = transferService;
        this.accountService = accountService;
    }


    @GetMapping
    public String showTransferForm(Model model,@AuthenticationPrincipal User currentUser) {
        List<Account> accounts = accountService.findAccountsOfCurrentUser(currentUser.getUsername());
        model.addAttribute("transaction", new TransferDTO());
        model.addAttribute("accounts",accounts);
        return "transactional";
    }

    @PostMapping
    public String transferFunds(@ModelAttribute("transaction") TransferDTO transferDTO, Model model) {
        try {
            transferService.transferFunds(transferDTO.getSenderAccountNumber(), transferDTO.getRecipientAccountNumber(), transferDTO.getAmount());
            model.addAttribute("success",true);
            return "redirect:/transaction?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/transaction?error=" + e.getMessage();
        }
    }


}