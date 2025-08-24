package com.joshroundy.cherry.service;

import com.joshroundy.cherry.client.CaptchaClient;
import com.joshroundy.cherry.dataobject.auth.LoginRequestDTO;
import com.joshroundy.cherry.dataobject.auth.UserResponseDTO;
import com.joshroundy.cherry.dataobject.entity.UserEntity;
import com.joshroundy.cherry.dataobject.auth.LoginResponseDTO;
import com.joshroundy.cherry.dataobject.auth.RegistrationDTO;
import com.joshroundy.cherry.repository.DateRepository;
import com.joshroundy.cherry.repository.MealItemRepository;
import com.joshroundy.cherry.repository.MealRepository;
import com.joshroundy.cherry.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorizationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CaptchaClient captchaClient;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${frontend.url}")
    private String frontendUrl;
    @Autowired
    private DateRepository dateRepository;
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private MealItemRepository mealItemRepository;

    public UserResponseDTO registerUser(RegistrationDTO registrationDTO){
// Disable captcha for mobile app registration. In hindsight, I should have used phone number verification instead
// because it has full mobile support and is more secure.
//        var captchaResponse = captchaClient.getCaptchaVerificationResponse(registrationDTO.getCaptchaToken());
//
//        if (captchaResponse.getBody() == null || !captchaResponse.getBody().isSuccess()) {
//            System.out.println("Captcha verification failed: " + captchaResponse.getBody().isSuccess() +
//                    ", Score: " + captchaResponse.getBody().getErrorCodes());
//            throw new RuntimeException("Captcha verification failed.");
//        }

        var emailVerificationToken = UUID.randomUUID().toString();
        var userEntity = UserEntity.builder()
                .username(registrationDTO.getUsername().toLowerCase())
                .passwordHash(passwordEncoder.encode(registrationDTO.getPassword()))
                .email(registrationDTO.getEmail().toLowerCase())
                .weight(registrationDTO.getWeight())
                .startingWeight(registrationDTO.getWeight())
                .isEmailVerified(false)
                .emailVerificationToken(emailVerificationToken)
                .emailVerificationTokenCreatedTS(LocalDateTime.now())
                .createdTS(Timestamp.valueOf(LocalDateTime.now()))
                .googleLoginToken(null)
                .googleRegistrationComplete(true).build();

        var existingUser = userRepository.findByEmail(userEntity.getEmail());
        if (existingUser.isPresent()) {
            var existingUserEntity = existingUser.get();
            if (!existingUserEntity.getIsEmailVerified() &&
                existingUserEntity.getEmailVerificationTokenCreatedTS().isBefore(LocalDateTime.now().minusDays(1))) {
                // If the account exists but is not verified and the token is expired, overwrite it
                userRepository.delete(existingUserEntity);
                userRepository.save(userEntity);
            } else {
                throw new DataIntegrityViolationException("Account already exists with the requested email");
            }
        } else {
            userRepository.save(userEntity);
        }

        sendVerificationEmail(userEntity.getEmail(), userEntity.getUsername(), emailVerificationToken);
        // Return without sensitive information
        return UserResponseDTO.builder()
                .userID(userEntity.getUserID())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .isEmailVerified(userEntity.getIsEmailVerified())
                .weight(userEntity.getWeight())
                .startingWeight(userEntity.getStartingWeight())
                .createdTS(userEntity.getCreatedTS())
                .build();
    }

    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) throws Exception {
        var username = loginRequestDTO.getUsername().toLowerCase();
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, loginRequestDTO.getPassword())
        );

        var userEntity = userRepository.findByUsername(username).get();
        if (!userEntity.getIsEmailVerified()) {
            throw new AccessDeniedException("Email not verified, please check your spam folder.");
        }

        var jwtToken = tokenService.generateJwt(authentication, userEntity.getUserID());
        // Only return non-sensitive user information
        return new LoginResponseDTO(UserResponseDTO.builder()
                .userID(userEntity.getUserID())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .isEmailVerified(userEntity.getIsEmailVerified())
                .weight(userEntity.getWeight())
                .startingWeight(userEntity.getStartingWeight())
                .createdTS(userEntity.getCreatedTS())
                .build(),
                jwtToken);
    }

    private void sendVerificationEmail(String toEmail, String username, String token) {
        String subject = String.format("Welcome to Cherry %s! Please verify your email", username);
        String verificationUrl = String.format("%s/verify?token=%s", frontendUrl, token);
        String body = "Click the link to verify your email: " + verificationUrl;

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom("cherry@joshroundy.dev");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    public void userPasswordReset(String email, String captchaToken) {
        // In the case of forgot password, we simply redirect the user to the web app to ensure
        // bot protection with captcha.
        var captchaResponse = captchaClient.getCaptchaVerificationResponse(captchaToken);
        if (captchaResponse.getBody() == null || !captchaResponse.getBody().isSuccess()) {
            System.out.println("Captcha verification failed: " + captchaResponse.getBody().isSuccess() +
                    ", Score: " + captchaResponse.getBody().getErrorCodes());
            throw new RuntimeException("Captcha verification failed.");
        }

        var user = userRepository.findByEmail(email.toLowerCase());
        if (user.isEmpty()) {
            return; // Do not disclose whether the email exists
        }
        var userEntity = user.get();
        String resetToken = UUID.randomUUID().toString();
        userEntity.setResetPasswordToken(resetToken);
        userEntity.setResetPasswordTokenCreatedTS(LocalDateTime.now());
        userRepository.save(userEntity);

        sendPasswordResetEmail(email, userEntity.getUsername(), resetToken);
    }

    private void sendPasswordResetEmail(String toEmail, String username, String token) {
        String subject = "Password Reset Request for " + username;
        String verificationUrl = String.format("%s/reset-password?token=%s", frontendUrl, token);
        String body = String.format("<p>Click the link to reset your password: %s</p> <p>NOTE: this link expires in one hour.</p>", verificationUrl);

        sendEmail(toEmail, subject, body);
    }

    public boolean resetPassword(String token, String newPassword) throws RuntimeException {
        var user = userRepository.findByResetPasswordToken(token);
        if (user.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }
        var userEntity = user.get();
        var passwordHash = passwordEncoder.encode(newPassword);
        if (userEntity.getResetPasswordTokenCreatedTS() == null ||
            userEntity.getResetPasswordTokenCreatedTS().isBefore(LocalDateTime.now().minusHours(1))) {
            throw new RuntimeException("Token expired");
        }
        if (passwordHash.equals(userEntity.getPasswordHash())) {
            throw new RuntimeException("New password cannot be the same as the old password");
        }
        userEntity.setPasswordHash(passwordHash);
        userEntity.setResetPasswordToken(null);
        userRepository.save(userEntity);
        return true;
    }

    public void userDeleteAccount(String email, String captchaToken) {
        // In the case of delete account, we simply redirect the user to the web app to ensure
        // bot protection with captcha.
        var captchaResponse = captchaClient.getCaptchaVerificationResponse(captchaToken);
        if (captchaResponse.getBody() == null || !captchaResponse.getBody().isSuccess()) {
            System.out.println("Captcha verification failed for delete account: " + captchaResponse.getBody().isSuccess() +
                    ", Score: " + captchaResponse.getBody().getErrorCodes());
            throw new RuntimeException("Captcha verification failed.");
        }

        var user = userRepository.findByEmail(email.toLowerCase());
        if (user.isEmpty()) {
            return; // Do not disclose whether the email exists
        }
        var userEntity = user.get();
        var deleteToken = UUID.randomUUID().toString();
        userEntity.setDeleteAccountToken(deleteToken);
        userEntity.setDeleteAccountTokenCreatedTS(LocalDateTime.now());
        userRepository.save(userEntity);

        sendDeleteAccountEmail(email, userEntity.getUsername(), deleteToken);
    }

    private void sendDeleteAccountEmail(String toEmail, String username, String token) {
        String subject = "Delete Account Request for " + username;
        String verificationUrl = String.format("%s/delete-account?token=%s", frontendUrl, token);
        String body = String.format("<p>Click the link to delete your account: %s</p> <p>NOTE: this link expires in one hour.</p>", verificationUrl);

        sendEmail(toEmail, subject, body);
    }

    public boolean deleteAccount(String token) throws RuntimeException {
        var user = userRepository.findByDeleteAccountToken(token);
        if (user.isEmpty()) {
            System.out.println("No user found with reset token: " + token);
            throw new RuntimeException("Invalid token");
        }
        var userEntity = user.get();
        if (userEntity.getDeleteAccountTokenCreatedTS() == null ||
                userEntity.getDeleteAccountTokenCreatedTS().isBefore(LocalDateTime.now().minusHours(1))) {
            throw new RuntimeException("Token expired");
        }
        userRepository.deleteByUserID(userEntity.getUserID());
        dateRepository.deleteByUserID(userEntity.getUserID());
        mealRepository.deleteByUserID(userEntity.getUserID());
        mealItemRepository.deleteByUserID(userEntity.getUserID());
        return true;
    }

    public boolean validateEmail(String token) {
        var user = userRepository.findByEmailVerificationToken(token);
        if (user.isEmpty()) {
            return false;
        }
        var userEntity = user.get();
        if (userEntity.getEmailVerificationTokenCreatedTS() == null ||
                userEntity.getEmailVerificationTokenCreatedTS().isBefore(LocalDateTime.now().minusDays(1))) {
            return false; // Token expired
        }
        userEntity.setIsEmailVerified(true);
        userEntity.setEmailVerificationToken(null);
        userRepository.save(userEntity);
        return true;
    }

    public boolean validateToken(String jwtToken) {
        return tokenService.validateJwt(jwtToken);
    }

    private void sendEmail(String toEmail, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom("cherry@joshroundy.dev");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send request email", e);
        }
    }
}
