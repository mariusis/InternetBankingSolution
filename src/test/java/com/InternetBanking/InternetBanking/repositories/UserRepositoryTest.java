package com.InternetBanking.InternetBanking.repositories;

import com.InternetBanking.InternetBanking.domain.Role;
import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        // Perform any setup or initialization before each test
    }

    @Test
    public void testFindByEmail() {
        // Create a sample user
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .roles(Collections.singleton(new Role("ROLE_USER")))
                .build();

        // Save the user to the repository
        userRepository.save(user);

        // Call the findByEmail method
        User foundUser = userRepository.findByEmail("john.doe@example.com");

        // Assert that the foundUser is not null and its email matches the search email
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals("john.doe@example.com", foundUser.getEmail());
    }

    @Test
    public void testFindByUserId() {
        // Create a sample user
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .roles(Collections.singleton(new Role("ROLE_USER")))
                .build();

        // Save the user to the repository
        userRepository.save(user);

        // Call the findByUserId method
        User foundUser = userRepository.findByUserId(user.getUserId());

        // Assert that the foundUser is not null and its userId matches the search userId
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getUserId(), foundUser.getUserId());
    }

    @Test
    public void testFindByVerificationToken() {
        // Create a sample user
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .verificationToken("TOKEN123")
                .roles(Collections.singleton(new Role("ROLE_USER")))
                .build();

        // Save the user to the repository
        userRepository.save(user);

        // Call the findByVerificationToken method
        User foundUser = userRepository.findByVerificationToken("TOKEN123");

        // Assert that the foundUser is not null and its verificationToken matches the search verificationToken
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals("TOKEN123", foundUser.getVerificationToken());
    }

}