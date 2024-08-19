package com.joshroundy.cherry.service;

import com.joshroundy.cherry.dataobject.auth.RegistrationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthorizationServiceIntegrationTest {
    @Autowired AuthorizationService subject;

    @Test
    void registerUser() {
        var username = "joshroundy";
        var password = "password";
        var email = "joshroundy@gmail.com";
        var weight = 196.3;
        var height = 69.7;
        var DOB = Date.valueOf("2003-02-28");
        var registrationDTO = RegistrationDTO.builder()
                .username(username)
                .password(password)
                .email(email)
                .height(height)
                .weight(weight)
                .dateOfBirth(DOB)
                .build();
        var UserEntity = subject.registerUser(registrationDTO);
        assertThat(UserEntity).isEqualToIgnoringGivenFields(UserEntity, "password","passwordHash", "userID");
    }
}