package com.InternetBanking.InternetBanking.web;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import com.InternetBanking.InternetBanking.services.AccountService;
import com.InternetBanking.InternetBanking.services.UserService;
import com.InternetBanking.InternetBanking.web.dto.AccountDto;
import com.InternetBanking.InternetBanking.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/accounts/create-account")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping
    public String showAccountCreationForm(Model model){
        model.addAttribute("account", new AccountDto());
        return "create-account";
    }

    @PostMapping
    public String createBankingAccount(@ModelAttribute("account") AccountDto accountDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println(email);
        accountDto.setEmail(email);
        accountService.save(accountDto);

        return "redirect:/accounts";
    }


}