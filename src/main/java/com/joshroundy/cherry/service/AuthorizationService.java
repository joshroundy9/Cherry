package com.joshroundy.cherry.service;

import com.joshroundy.cherry.dataobject.auth.LoginRequestDTO;
import com.joshroundy.cherry.dataobject.entity.UserEntity;
import com.joshroundy.cherry.dataobject.auth.LoginResponseDTO;
import com.joshroundy.cherry.dataobject.auth.RegistrationDTO;
import com.joshroundy.cherry.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );

        var userEntity = userRepository.findByUsername(loginRequestDTO.getUsername()).get();

        var jwtToken = tokenService.generateJwt(authentication, userEntity.getUserID());

        return new LoginResponseDTO(userEntity, jwtToken);
    }

    public boolean validateToken(String jwtToken) {
        return tokenService.validateJwt(jwtToken);
    }
}
