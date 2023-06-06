package com.InternetBanking.InternetBanking.services;

import com.InternetBanking.InternetBanking.domain.Role;
import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import com.InternetBanking.InternetBanking.services.RegistrationVerificationServiceImpl;
import com.InternetBanking.InternetBanking.services.UserServiceImpl;
import com.InternetBanking.InternetBanking.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RegistrationVerificationServiceImpl verification;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() throws MessagingException {
        // Given
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setFirstName("John");
        registrationDto.setLastName("Doe");
        registrationDto.setEmail("johndoe@example.com");
        registrationDto.setPassword("password");

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn(encodedPassword);

        String verificationToken = "verificationToken";
        doNothing().when(verification).sendVerificationEmail(anyString(), anyString());

        // When
        User savedUser = userService.save(registrationDto);

        // Then
        assertNotNull(savedUser);
        assertEquals(registrationDto.getFirstName(), savedUser.getFirstName());
        assertEquals(registrationDto.getLastName(), savedUser.getLastName());
        assertEquals(registrationDto.getEmail(), savedUser.getEmail());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(1, savedUser.getRoles().size());
        assertTrue(savedUser.getRoles().contains(new Role("ROLE_USER")));

        // Verify that the user is saved with the verification token
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertNotNull(capturedUser.getVerificationToken());
        assertEquals(savedUser.getVerificationToken(), capturedUser.getVerificationToken());

        // Verify that the verification email is sent
        verify(verification).sendVerificationEmail(savedUser.getEmail(), anyString());
    }

    @Test
    void testLoadUserByUsername() {
        // Given
        String email = "johndoe@example.com";
        String encodedPassword = "encodedPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRoles(Collections.singletonList(new Role("ROLE_USER")));

        when(userRepository.findByEmail(email)).thenReturn(user);

        // When
        UserDetails userDetails = userService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(encodedPassword, userDetails.getPassword());

        Collection<? extends SimpleGrantedAuthority> authorities = (Collection<? extends SimpleGrantedAuthority>) userDetails.getAuthorities();
        Collection<Role> userRoles = user.getRoles();
        assertEquals(userRoles.size(), authorities.size());
        for (Role role : userRoles) {
            assertTrue(authorities.contains(new SimpleGrantedAuthority(role.getName())));
        }
    }
}