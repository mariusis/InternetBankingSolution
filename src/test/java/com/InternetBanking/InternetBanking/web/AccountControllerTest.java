package com.InternetBanking.InternetBanking.web;

import com.InternetBanking.InternetBanking.domain.Account;
import com.InternetBanking.InternetBanking.domain.Role;
import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import com.InternetBanking.InternetBanking.services.AccountService;
import com.InternetBanking.InternetBanking.web.AccountController;
import com.InternetBanking.InternetBanking.web.dto.AccountDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testShowAccounts() throws Exception {
        // Create a mock user
        User mockUser = User.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .verified(true)
                .roles(Collections.singleton(new Role("ROLE_USER")))
                .build();

        // Mock the current user
        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUser, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock the userRepository.findByEmail method
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(mockUser);

        // Mock the accountService.findAccountsOfCurrentUser method
        List<Account> mockAccounts = Arrays.asList(
                new Account("Savings",1L,"Account1","dollar"),
                new Account("Debit",2L, "Account2","Dolar")
        );
        Mockito.when(accountService.findAccountsOfCurrentUser(Mockito.anyString())).thenReturn(mockAccounts);

        // Perform the GET request to "/accounts"
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("accounts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("accounts"))
                .andExpect(MockMvcResultMatchers.model().attribute("accounts", mockAccounts));

        // Verify that the userRepository.findByEmail method was called
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(Mockito.anyString());

        // Verify that the accountService.findAccountsOfCurrentUser method was called
        Mockito.verify(accountService, Mockito.times(1)).findAccountsOfCurrentUser(Mockito.anyString());
    }

    @Test
    public void testShowAccountCreationForm() throws Exception {
        // Perform the GET request to "/accounts/create-account"
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/create-account"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create-account"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("account"))
                .andExpect(MockMvcResultMatchers.model().attribute("account", Matchers.instanceOf(AccountDto.class)));
    }

    @Test
    public void testCreateBankingAccount() throws Exception {
        // Create a mock AccountDto
        AccountDto mockAccountDto = new AccountDto();
        mockAccountDto.setAccountName("Account 1");

        // Perform the POST request to "/create-account" with the mock AccountDto
        mockMvc.perform(MockMvcRequestBuilders.post("/create-account")
                .flashAttr("account", mockAccountDto))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/accounts"));

        // Verify that the accountService.save method was called
        Mockito.verify(accountService, Mockito.times(1)).save(Mockito.eq(mockAccountDto));
    }
}