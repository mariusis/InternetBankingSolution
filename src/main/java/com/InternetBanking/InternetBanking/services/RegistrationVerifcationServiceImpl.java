package com.InternetBanking.InternetBanking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
@Service
public class RegistrationVerifcationServiceImpl {

    // Autowire the JavaMailSender bean in your controller or service
    @Autowired
    private JavaMailSender javaMailSender;

    // Create a method to send the verification email
    public void sendVerificationEmail(String recipientEmail, String verificationLink) throws MessagingException {
        // Create a MimeMessage object
        MimeMessage message = javaMailSender.createMimeMessage();
        // Use the MimeMessageHelper class to set the email details and add HTML content
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipientEmail);
        helper.setSubject("Registration Verification");
        helper.setText("<html><body><p>Dear User,</p><p>Thank you for registering with our website. Please click the link below to verify your account:</p><p><a>'" + verificationLink + "'</a></p></body></html>", true);
        // Send the email using the JavaMailSender interface
        javaMailSender.send(message);
    }
}