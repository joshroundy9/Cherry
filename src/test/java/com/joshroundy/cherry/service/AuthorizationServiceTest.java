package com.joshroundy.cherry.service;

import com.joshroundy.cherry.dataobject.auth.RegistrationDTO;
import com.joshroundy.cherry.dataobject.entity.UserEntity;
import com.joshroundy.cherry.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthorizationServiceTest {
    @InjectMocks AuthorizationService subject;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    TokenService tokenService;
    String passwordHash;
    UserEntity userEntity;
    RegistrationDTO registrationDTO;
    @BeforeEach
    void setUp() {
        passwordHash = "passwordHash";
        registrationDTO = RegistrationDTO.builder()
                .dateOfBirth(Date.valueOf("2024-05-06"))
                .email("randomemail@gmail.com")
                .height(34.98)
                .weight(450.5)
                .password("password")
                .username("username").build();
        userEntity = UserEntity.builder()
                .userID(45)
                .height(registrationDTO.getHeight())
                .weight(registrationDTO.getWeight())
                .username(registrationDTO.getUsername())
                .passwordHash(passwordHash)
                .dateOfBirth(registrationDTO.getDateOfBirth())
                .email(registrationDTO.getEmail())
                .build();
    }
    @Test
    void registerUserTest() {
        when(passwordEncoder.encode(any())).thenReturn(passwordHash);
        when(userRepository.save(any())).thenReturn(userEntity);
        assertThat(subject.registerUser(registrationDTO)).isEqualTo(userEntity);
    }
    @Test void loginUserTest_happyPath() {
        var uuid = UUID.randomUUID().toString();
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(tokenService.generateJwt(any())).thenReturn(uuid);
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(userEntity));
        var actual = subject.loginUser("username", "password");
        assertThat(actual.getUser()).isEqualTo(userEntity);
        assertThat(actual.getJwt()).isEqualTo(uuid);
    }
    @Test void loginUserTest_sadPath_throwsAuthenticationException() {
        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationCredentialsNotFoundException(""));
        var actual = subject.loginUser("username", "password");
        assertThat(actual.getUser()).isNull();
        assertThat(actual.getJwt()).isEqualTo("");
        verifyNoInteractions(tokenService);
        verifyNoInteractions(userRepository);
    }
}
