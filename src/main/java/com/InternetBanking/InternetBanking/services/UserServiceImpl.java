package com.InternetBanking.InternetBanking.services;

import com.InternetBanking.InternetBanking.domain.Role;
import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import com.InternetBanking.InternetBanking.web.dto.UserRegistrationDto;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RegistrationVerifcationServiceImpl verification;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RegistrationVerifcationServiceImpl verification) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verification = verification;
    }

    @Override
    public User save(UserRegistrationDto registrationDto) throws MessagingException {
        User user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("ROLE_USER")));

        // Generate a unique verification token
        String verificationToken = UUID.randomUUID().toString();

        // Save the user details in the database with the verification token
        user.setVerificationToken(verificationToken);
        userRepository.save(user);
        String verificationLink = "http://localhost:8080/verify?token=" + verificationToken;
        verification.sendVerificationEmail(user.getEmail(),verificationLink );

        return user;
    }


@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }


}