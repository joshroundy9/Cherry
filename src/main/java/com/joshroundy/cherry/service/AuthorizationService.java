package com.joshroundy.cherry.service;

import com.joshroundy.cherry.dataobject.entity.UserEntity;
import com.joshroundy.cherry.dataobject.auth.LoginResponseDTO;
import com.joshroundy.cherry.dataobject.auth.RegistrationDTO;
import com.joshroundy.cherry.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    public LoginResponseDTO loginUser(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);

            return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);

        } catch(AuthenticationException e){
            return new LoginResponseDTO(null, "");
        }
    }
}
