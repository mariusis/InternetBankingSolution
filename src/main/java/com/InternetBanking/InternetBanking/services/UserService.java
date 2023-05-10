package com.InternetBanking.InternetBanking.services;

import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.mail.MessagingException;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto) throws MessagingException;
}
