package com.InternetBanking.InternetBanking.services;

import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
@Service
public class SecurityServiceImpl implements SecurityService{
    @Autowired
    private UserRepository userRepository;

    public SecurityServiceImpl() {
    }


    @Override
    public boolean isVerifiedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        return user != null && user.isVerified();
    }
}
