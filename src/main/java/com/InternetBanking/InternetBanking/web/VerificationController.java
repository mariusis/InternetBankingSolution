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
    private UserRepository userRepository;

    @Autowired
    private RegistrationVerificationServiceImpl registrationVerificationService;

    @GetMapping("/unverified")
    public String unverified(Model model) {
        model.addAttribute("successMessage", "");
        return "unverified";
    }

    @PostMapping("/unverified")
    public String resendVerification( Model model) throws MessagingException {
        registrationVerificationService.reSendVerificationEmail();

        model.addAttribute("successMessage", "Verification email has been sent successfully!");
        return "unverified";

    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String verificationToken) {
        // Find the user by verification token
        User user = userRepository.findByVerificationToken(verificationToken);

        if (user == null) {
            return "verification-failed";
        }

        // Set the user's verified status to true
        user.setVerified(true);
        userRepository.save(user);

        return "verification-successful";
    }
}