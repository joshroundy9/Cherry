package com.joshroundy.cherry.controller;

import com.joshroundy.cherry.dataobject.auth.LoginResponseDTO;
import com.joshroundy.cherry.dataobject.auth.RegistrationDTO;
import com.joshroundy.cherry.dataobject.entity.UserEntity;
import com.joshroundy.cherry.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    @Autowired
    private AuthorizationService authenticationService;

    @PostMapping("/register")
    public UserEntity registerUser(@RequestBody RegistrationDTO body){
        return authenticationService.registerUser(body);
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody RegistrationDTO body){
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }
}
