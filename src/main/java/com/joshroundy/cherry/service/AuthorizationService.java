package com.joshroundy.cherry.service;

import com.joshroundy.cherry.dataobject.auth.LoginRequestDTO;
import com.joshroundy.cherry.dataobject.entity.UserEntity;
import com.joshroundy.cherry.dataobject.auth.LoginResponseDTO;
import com.joshroundy.cherry.dataobject.auth.RegistrationDTO;
import com.joshroundy.cherry.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorizationService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private JavaMailSender mailSender;

    @Value("${frontend.url}")
    private String frontendUrl;

    public UserEntity registerUser(RegistrationDTO registrationDTO){
        var emailVerificationToken = UUID.randomUUID().toString();
        var userEntity = UserEntity.builder()
                .username(registrationDTO.getUsername())
                .passwordHash(passwordEncoder.encode(registrationDTO.getPassword()))
                .dateOfBirth(registrationDTO.getDateOfBirth())
                .email(registrationDTO.getEmail())
                .weight(registrationDTO.getWeight())
                .isEmailVerified(false)
                .emailVerificationToken(emailVerificationToken).build();

        userRepository.save(userEntity);

        sendVerificationEmail(userEntity.getEmail(), emailVerificationToken);

        return userEntity;
    }

    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) throws Exception {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );

        var userEntity = userRepository.findByUsername(loginRequestDTO.getUsername()).get();
        if (!userEntity.getIsEmailVerified()) {
            throw new AccessDeniedException("Email not verified");
        }

        var jwtToken = tokenService.generateJwt(authentication, userEntity.getUserID());

        return new LoginResponseDTO(userEntity, jwtToken);
    }

    private void sendVerificationEmail(String toEmail, String token) {
        String subject = "Verify your email";
        String verificationUrl = String.format("%s/verify?token=%s", frontendUrl, token);
        String body = "Click the link to verify your email: " + verificationUrl;

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    public boolean validateEmail(String token) {
        var user = userRepository.findByEmailVerificationToken(token);
        if (user == null) {
            return false;
        }
        user.setIsEmailVerified(true);
        user.setEmailVerificationToken(null);
        userRepository.save(user);
        return true;
    }

    public boolean validateToken(String jwtToken) {
        return tokenService.validateJwt(jwtToken);
    }
}
