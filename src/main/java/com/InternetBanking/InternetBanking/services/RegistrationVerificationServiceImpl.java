package com.InternetBanking.InternetBanking.services;

import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
@Service
public class RegistrationVerificationServiceImpl {


    private final  JavaMailSender javaMailSender;
    private final  UserRepository userRepository;

    public RegistrationVerificationServiceImpl(JavaMailSender javaMailSender, UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
    }

    // Create a method to send the verification email
    public void sendVerificationEmail(String recipientEmail, String verificationLink) throws MessagingException {
        // Create a MimeMessage object
        MimeMessage message = javaMailSender.createMimeMessage();
        // Use the MimeMessageHelper class to set the email details and add HTML content
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipientEmail);
        helper.setSubject("Registration Verification");
        helper.setText("<html><body><p>Dear User,</p><p>Thank you for registering with our website. Please click the link below to verify your account:</p><p><a href=\"" + verificationLink + "\">" + verificationLink + "</a></p></body></html>", true);
        // Send the email using the JavaMailSender interface
        javaMailSender.send(message);
    }

    public boolean reSendVerificationEmail() throws MessagingException {
        User user = userRepository.findByEmail( SecurityContextHolder.getContext().getAuthentication().getName());

        String verificationToken = user.getVerificationToken();
        String verificationLink = "http://localhost:8080/verify?token=" + verificationToken;
        String recipientEmail = user.getEmail();
        this.sendVerificationEmail(recipientEmail,verificationLink);
        return true;
    }
}
