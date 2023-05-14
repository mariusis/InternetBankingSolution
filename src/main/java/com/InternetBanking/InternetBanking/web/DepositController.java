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
    private final DepositService depositService;
    private final AccountService accountService;

    public DepositController(DepositService depositService, AccountService accountService) {
        this.depositService = depositService;
        this.accountService = accountService;
    }

    @GetMapping
    public String showDepositForm(Model model,@AuthenticationPrincipal User currentUser) {

        List<Account> accounts = accountService.findAccountsOfCurrentUser(currentUser.getUsername());
        model.addAttribute("deposit", new DepositDTO());
        model.addAttribute("accounts",accounts);
        return "deposit";
    }

    @PostMapping
    public String deposit(@ModelAttribute("deposit") DepositDTO depositDTO, Model model) throws AccountNotFoundException {
        try {
            depositService.deposit(depositDTO.getAccountNumber(), depositDTO.getAmount());
            model.addAttribute("success",true);
            return "redirect:/deposit?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/deposit?error=" + e.getMessage();
        }

    }
}