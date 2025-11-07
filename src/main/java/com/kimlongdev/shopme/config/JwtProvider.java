package com.kimlongdev.shopme.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtProvider {

    // HMAC = hash + secret key
    private SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    /*
       GrantedAuthority là một interface trong Spring Security, dùng để biểu diễn quyền (authority) mà người dùng có.
       Mỗi GrantedAuthority tượng trưng cho một quyền hoặc vai trò cụ thể (ví dụ: "ROLE_ADMIN", "ROLE_USER", "READ_PRIVILEGE"...).
     */
    public String generateToken(Authentication auth) {
        // Get authorities
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities); // Convert to string

        String jwt= Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+86400000))
                .claim("email",auth.getName())
                .claim("authorities", roles)
                .signWith(secretKey) // Sign with secret key
                .compact(); // Build JWT
        return jwt;

    }

    public String getEmailFromJwtToken(String jwt) {
        jwt=jwt.substring(7); // Remove "Bearer " prefix

        // Claims is the payload part of JWT
        Claims claims=Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwt).getBody();
        String email=String.valueOf(claims.get("email"));

        return email;
    }

    // Convert collection of GrantedAuthority to a comma-separated string
    public String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> auths=new HashSet<>();

        // Duyệt qua từng quyền và thêm vào tập hợp
        for(GrantedAuthority authority:collection) {
            auths.add(authority.getAuthority());
        }

        // Nối các quyền thành một chuỗi
        return String.join(",",auths);
    }

}
