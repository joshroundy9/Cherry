package com.joshroundy.cherry.service;

import com.joshroundy.cherry.annotation.IntegrationTest;
import com.joshroundy.cherry.dataobject.auth.LoginRequestDTO;
import com.joshroundy.cherry.dataobject.auth.RegistrationDTO;
import com.joshroundy.cherry.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class AuthorizationServiceIntegrationTest {
    @Autowired AuthorizationService subject;
    @Autowired
    UserRepository userRepository;

    @Test
    @Disabled
    void registerAndLoginUser() throws Exception {
        var username = "joshroundy";
        var password = "password";
        var loginRequestDTO = LoginRequestDTO.builder()
                .username(username)
                .password(password)
                .build();
        var email = "joshroundy@gmail.com";
        var weight = 196.3;
        var registrationDTO = RegistrationDTO.builder()
                .username(username)
                .password(password)
                .email(email)
                .weight(weight)
                .build();
        var userEntity = subject.registerUser(registrationDTO);
        assertThat(userEntity).isEqualToIgnoringGivenFields(userEntity, "password","passwordHash", "userID");
        var loginResponse = subject.loginUser(loginRequestDTO);
        assertThat(loginResponse.getUser()).isEqualTo(userEntity);
    }
}