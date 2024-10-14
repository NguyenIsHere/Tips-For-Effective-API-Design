package com.example.demo.config;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Service
public class JwtProvider {
    private SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    public String generatedToken(Authentication authentication){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String role = populateAuthorities(authorities);

        String jwt = Jwts.builder()
                .claim("email", authentication.getName())
                .claim("authorities", role)
                .signWith(secretKey)
                .compact();
        return jwt;
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority>authorities) {
        Set<String> auths = new HashSet<>();
        for(GrantedAuthority authority:authorities){
            auths.add(authority.getAuthority());
        }

        return String.join(",", auths);
    }

    public String getEmailFromJwt(String jwt) {
        jwt = jwt.substring(7);
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();

        String email = String.valueOf(claims.get("email"));
        System.out.println(email);
        return email;
    }

}
