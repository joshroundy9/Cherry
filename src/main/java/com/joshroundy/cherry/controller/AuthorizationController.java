package com.joshroundy.cherry.controller;

import com.joshroundy.cherry.dataobject.auth.LoginRequestDTO;
import com.joshroundy.cherry.dataobject.auth.LoginResponseDTO;
import com.joshroundy.cherry.dataobject.auth.RegistrationDTO;
import com.joshroundy.cherry.service.AuthorizationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import static com.joshroundy.cherry.constant.AuthorizationConstants.PASSWORD_ERROR_MESSAGE;
import static com.joshroundy.cherry.constant.AuthorizationConstants.PASSWORD_REGEX;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    @Autowired
    private AuthorizationService authenticationService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Invalid input");
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationDTO body){
        try {
            var user = authenticationService.registerUser(body);
            return ResponseEntity.ok(user);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Username or email already exists");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody LoginRequestDTO body) throws Exception {
        return authenticationService.loginUser(body);
    }

    @PostMapping("/validate")
    public boolean loginUser(@RequestHeader("JWT-Token") String jwtToken) {
        return authenticationService.validateToken(jwtToken);
    }
    @PostMapping("/email/validate")
    public ResponseEntity<String> validateEmail(@RequestHeader("Email") String email) {
        if (authenticationService.validateEmail(email)) {
            return ResponseEntity.ok("Email verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Cannot verify email: Invalid token");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestHeader("Email") String email) {
        try {
            authenticationService.userPasswordReset(email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Password reset link sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestHeader("Token") String token, @RequestHeader("Password") String password) {
        if (token == null || token.isEmpty() || password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
        if (!password.matches(PASSWORD_REGEX)) {
            return ResponseEntity.badRequest().body(PASSWORD_ERROR_MESSAGE);
        }
        try {
            var success = authenticationService.resetPassword(token, password);
            if (success) {
                return ResponseEntity.ok("Password successfully updated");
            }
            return ResponseEntity.badRequest().body("Password reset failed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
