package com.joshroundy.cherry.dataobject.auth;

import com.joshroundy.cherry.util.AuthorizationConstants;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.joshroundy.cherry.util.AuthorizationConstants.*;

@Getter
@Setter
@Builder
public class LoginRequestDTO {
    @Pattern(regexp = USERNAME_REGEX, message = USERNAME_ERROR_MESSAGE)
    private String username;
    private String password;
}
