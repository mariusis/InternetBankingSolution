package com.InternetBanking.InternetBanking.web;

import com.InternetBanking.InternetBanking.services.UserService;
import com.InternetBanking.InternetBanking.web.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private UserService userService; // Service for user-related logic

    // Constructor Dependency Injection (DI)
    public UserRegistrationController(UserService userService) {
        this.userService = userService; // Initializing UserService via DI
    }

    // Handles the route for displaying the user registration form
    @GetMapping
    public String showRegistrationForm(Model model) {
        // Add an empty UserRegistrationDto to the model for rendering the registration form
        model.addAttribute("user", new UserRegistrationDto());

        // Render the 'registration' HTML template to display the registration form
        return "registration";
    }

    // Handles the user registration submission
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) throws MessagingException {
        if (userService.save(registrationDto) == null) {
            // If registration fails, redirect the user back to the registration page with a "failed" query parameter
            return "redirect:/registration?failed";
        }

        // If registration succeeds, save the user and redirect to the registration page with a "success" query parameter
        userService.save(registrationDto);
        return "redirect:/registration?success";
    }
}

