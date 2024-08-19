package com.joshroundy.cherry.dataobject.auth;

import com.joshroundy.cherry.dataobject.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
    private UserEntity user;
    private String jwt;
}