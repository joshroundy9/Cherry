package com.joshroundy.cherry.service;

import com.joshroundy.cherry.client.CaptchaClient;
import com.joshroundy.cherry.dataobject.auth.LoginRequestDTO;
import com.joshroundy.cherry.dataobject.auth.RegistrationDTO;
import com.joshroundy.cherry.dataobject.auth.UserResponseDTO;
import com.joshroundy.cherry.dataobject.client.CaptchaClientResponseDTO;
import com.joshroundy.cherry.dataobject.entity.UserEntity;
import com.joshroundy.cherry.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    @Mock
    CaptchaClient captchaClient;
    @Mock
    JavaMailSender mailSender;
    MimeMessage mimeMessage;
    String passwordHash;
    UserEntity userEntity;
    UserResponseDTO userResponseDTO;
    RegistrationDTO registrationDTO;
    LoginRequestDTO loginRequestDTO;
    Integer userID;
    @BeforeEach
    void setUp() {
        userID = 45;
        passwordHash = "passwordHash";
        registrationDTO = RegistrationDTO.builder()
                .email("randomemail@gmail.com")
                .weight(450.5)
                .password("password")
                .username("username").build();
        userEntity = UserEntity.builder()
                .userID(userID)
                .weight(registrationDTO.getWeight())
                .username(registrationDTO.getUsername())
                .passwordHash(passwordHash)
                .email(registrationDTO.getEmail())
                .isEmailVerified(false)
                .build();
        userResponseDTO = UserResponseDTO.builder()
                .userID(userEntity.getUserID())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .isEmailVerified(true)
                .weight(userEntity.getWeight())
                .build();
        loginRequestDTO = LoginRequestDTO.builder()
                .username("username")
                .password("password")
                .build();
        mimeMessage = mock(MimeMessage.class);
    }
    @Test
    void registerUserTest() {
        when(passwordEncoder.encode(any())).thenReturn(passwordHash);
        when(userRepository.save(any())).thenReturn(userEntity);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(captchaClient.getCaptchaVerificationResponse(any())).thenReturn(
                ResponseEntity.ok(
                        CaptchaClientResponseDTO.builder().success(true).build()
                ));
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThat(subject.registerUser(registrationDTO)).usingRecursiveComparison()
                .ignoringFields("emailVerificationToken", "emailVerificationTokenCreatedTS", "userID").isEqualTo(userEntity);
    }
    @Test void loginUserTest_happyPath() throws Exception {
        var uuid = UUID.randomUUID().toString();
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(tokenService.generateJwt(any(), any())).thenReturn(uuid);
        userEntity.setIsEmailVerified(true);
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(userEntity));
        var actual = subject.loginUser(loginRequestDTO);
        assertThat(actual.getUser()).usingRecursiveComparison().isEqualTo(userResponseDTO);
        assertThat(actual.getJwt()).isEqualTo(uuid);
    }
    @Test void loginUserTest_sadPath_throwsAuthenticationException() {
        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationCredentialsNotFoundException(""));
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> {
            subject.loginUser(loginRequestDTO);
        });
        verifyNoInteractions(tokenService);
        verifyNoInteractions(userRepository);
    }
}
