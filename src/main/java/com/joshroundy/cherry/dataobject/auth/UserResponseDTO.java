package com.joshroundy.cherry.dataobject.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Builder
public class UserResponseDTO {
    private Integer userID;
    private String username;
    private String email;
    private Date dateOfBirth;
    private Boolean isEmailVerified;
    private Double weight;
}
