package com.joshroundy.cherry.dataobject.auth;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.joshroundy.cherry.constant.AuthorizationConstants.*;

@Getter
@Setter
@Builder
public class LoginRequestDTO {
    private String username;
    private String password;
}
