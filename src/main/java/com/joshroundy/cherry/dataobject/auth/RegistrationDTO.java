package com.joshroundy.cherry.dataobject.auth;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

import static com.joshroundy.cherry.constant.AuthorizationConstants.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RegistrationDTO {
    @Pattern (regexp = USERNAME_REGEX, message = USERNAME_ERROR_MESSAGE)
    private String username;
    @Pattern (regexp = EMAIL_REGEX, message = EMAIL_ERROR_MESSAGE)
    private String email;
    @Pattern (regexp = PASSWORD_REGEX, message = PASSWORD_ERROR_MESSAGE)
    private String password;
    private Date dateOfBirth;
    @Max(value = WEIGHT_MAX, message = WEIGHT_ERROR_MESSAGE)
    private Double weight;

    public String toString(){
        return "Registration info: username: " + this.username + " email: " + this.email + " password: " + this.password;
    }
}
