package com.joshroundy.cherry.dataobject.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    private String passwordHash;
    private Double weight;
    private Double startingWeight;
    private Boolean isEmailVerified;
    private String emailVerificationToken;
    private LocalDateTime emailVerificationTokenCreatedTS;
    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenCreatedTS;
    private Timestamp createdTS;
    private String googleLoginToken;
    private Boolean googleRegistrationComplete;
    private String deleteAccountToken;
    private LocalDateTime deleteAccountTokenCreatedTS;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return getPasswordHash();
    }
}
