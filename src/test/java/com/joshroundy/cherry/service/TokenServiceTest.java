package com.joshroundy.cherry.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenServiceTest {
    JwtDecoder jwtDecoder;
    TokenService subject;
    String dummyToken;


    @BeforeEach
    void beforeEach() {
        jwtDecoder = mock(JwtDecoder.class);
        subject = new TokenService(null, jwtDecoder);
        dummyToken = "dummyToken";
    }

    @Test
    void validateMatchingUserId_returnsTrue_whenUserIdMatches() {
        var userId = 42;
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        var jwt = mock(Jwt.class);
        when(jwt.getClaims()).thenReturn(claims);
        when(jwtDecoder.decode(dummyToken)).thenReturn(jwt);

        assertThat(subject.validateMatchingUserId(dummyToken, userId)).isTrue();
    }

    @Test
    void validateMatchingUserId_returnsFalse_whenUserIdDoesNotMatch() {
        var userId = 42;
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 99);

        var jwt = mock(Jwt.class);
        when(jwt.getClaims()).thenReturn(claims);
        when(jwtDecoder.decode(dummyToken)).thenReturn(jwt);

        assertThat(subject.validateMatchingUserId(dummyToken, userId)).isFalse();
    }

    @Test
    void validateMatchingUserId_returnsFalse_whenUserIdMissing() {
        var userId = 42;
        Map<String, Object> claims = new HashMap<>();

        var jwt = mock(Jwt.class);
        when(jwt.getClaims()).thenReturn(claims);
        when(jwtDecoder.decode(dummyToken)).thenReturn(jwt);

        assertThat(subject.validateMatchingUserId(dummyToken, userId)).isFalse();
    }
}
