package com.joshroundy.cherry.service;

import com.joshroundy.cherry.dataobject.auth.LoginRequestDTO;
import com.joshroundy.cherry.dataobject.entity.UserEntity;
import com.joshroundy.cherry.dataobject.auth.LoginResponseDTO;
import com.joshroundy.cherry.dataobject.auth.RegistrationDTO;
import com.joshroundy.cherry.repository.UserRepository;
import com.joshroundy.cherry.util.AuthorizationConstants;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.processing.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@AllArgsConstructor
public class AuthorizationService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    public UserEntity registerUser(RegistrationDTO registrationDTO){
        var userEntity = UserEntity.builder()
                .username(registrationDTO.getUsername())
                .passwordHash(passwordEncoder.encode(registrationDTO.getPassword()))
                .dateOfBirth(registrationDTO.getDateOfBirth())
                .email(registrationDTO.getEmail())
                .height(registrationDTO.getHeight())
                .weight(registrationDTO.getWeight()).build();
        return userRepository.save(userEntity);
    }

    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO){
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
            );

            var jwtToken = tokenService.generateJwt(authentication);

            return new LoginResponseDTO(userRepository.findByUsername(loginRequestDTO.getUsername()).get(), jwtToken);

        } catch(AuthenticationException e) {
            return new LoginResponseDTO(null, "");
        }
    }
}
