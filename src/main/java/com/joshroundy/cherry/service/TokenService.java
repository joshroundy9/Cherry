package com.joshroundy.cherry.service;
import java.time.Instant;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TokenService {

    private JwtEncoder jwtEncoder;

    private JwtDecoder jwtDecoder;

    public String generateJwt(Authentication auth, Integer userId) {

        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(auth.getName())
                .claim("roles", scope)
                .claim("userId", userId)
                .expiresAt(now.plusSeconds(3600 * 24 * 7)) // Token valid for 1 week
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean validateJwt(String jwtToken) {
        jwtDecoder.decode(jwtToken);
        return true;
    }

    public boolean validateMatchingUserId(String jwtToken, Integer userId) {
        var claims = jwtDecoder.decode(jwtToken).getClaims();
        var tokenUserId = (Integer) claims.get("userId");
        return tokenUserId != null && tokenUserId.equals(userId);
    }
}
