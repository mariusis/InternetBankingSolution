package com.InternetBanking.InternetBanking.web;

import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerificationController {
    @Autowired
    private UserRepository userRepository;

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
    @GetMapping("/unverified")
    public String unverified() {
        return "unverified";
    }
}