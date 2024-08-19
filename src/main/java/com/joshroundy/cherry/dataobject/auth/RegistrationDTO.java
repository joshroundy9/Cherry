package com.joshroundy.cherry.dataobject.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationDTO {
    private String username;
    private String email;
    private String password;
    private Date dateOfBirth;
    private Double Height;
    private Double Weight;

    public String toString(){
        return "Registration info: username: " + this.username + " email: " + this.email + " password: " + this.password;
    }
}
