package com.InternetBanking.InternetBanking.web;

import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import com.InternetBanking.InternetBanking.services.RegistrationVerificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
@Controller
public class VerificationController {

    @Autowired
    private UserRepository userRepository; // Repository for accessing user data

    @Autowired
    private RegistrationVerificationServiceImpl registrationVerificationService; // Service for registration verification logic

    // Handles the route for displaying the page when the user is unverified
    @GetMapping("/unverified")
    public String unverified(Model model) {
        model.addAttribute("successMessage", ""); // Initialize the success message
        return "unverified"; // Render the 'unverified' HTML template
    }

    // Handles the user's request to resend the verification email
    @PostMapping("/unverified")
    public String resendVerification(Model model) throws MessagingException {
        // Call the service to resend the verification email
        registrationVerificationService.reSendVerificationEmail();

        // Add a success message to the model
        model.addAttribute("successMessage", "Verification email has been sent successfully!");

        // Render the 'unverified' HTML template
        return "unverified";
    }

    // Handles the route for verifying the user's email
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String verificationToken) {
        // Find the user by verification token using the UserRepository
        User user = userRepository.findByVerificationToken(verificationToken);

        if (user == null) {
            return "verification-failed"; // If user not found, render the 'verification-failed' template
        }

        // Set the user's verified status to true
        user.setVerified(true);
        userRepository.save(user);

        return "verification-successful"; // Render the 'verification-successful' template
    }
}
