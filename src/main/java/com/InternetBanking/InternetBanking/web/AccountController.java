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

    private final AccountService accountService;
    private final UserRepository userRepository;
    public AccountController(AccountService accountService, UserRepository userRepository) {
        this.accountService = accountService;
        this.userRepository = userRepository;
    }
    @GetMapping("/accounts")
    public String showAccounts(Model model, @AuthenticationPrincipal User currentUser) {
        com.InternetBanking.InternetBanking.domain.User user = userRepository.findByEmail(currentUser.getUsername());
        List<Account> accounts = accountService.findAccountsOfCurrentUser(currentUser.getUsername());
        model.addAttribute("accounts", accounts);
        return "accounts";
    }

    @GetMapping("/accounts/create-account")
    public String showAccountCreationForm(Model model){
        model.addAttribute("account", new AccountDto());
        return "create-account";
    }

    @PostMapping("/create-account")
    public String createBankingAccount(@ModelAttribute("account") AccountDto accountDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        accountDto.setEmail(email);
        accountService.save(accountDto);
        return "redirect:/accounts";
    }



}