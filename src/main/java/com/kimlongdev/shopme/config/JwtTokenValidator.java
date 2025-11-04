package com.kimlongdev.shopme.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException
    {
        // Get JWT from Authorization header
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if(jwt!=null) {
            jwt=jwt.substring(7); // Remove "Bearer " prefix

            try {
                // Create secret key from constant
                SecretKey key= Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

                // Parse claims from JWT
                Claims claims= Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                // Extract email and authorities from claims
                String email=String.valueOf(claims.get("email"));
                String authorities=String.valueOf(claims.get("authorities"));

                // Convert comma-separated string to list of GrantedAuthority
                List<GrantedAuthority> auths= AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                Authentication athentication=new UsernamePasswordAuthenticationToken(email,null, auths);

                // Spring Security Context dùng để lưu trữ thông tin xác thực của người dùng hiện tại
                SecurityContextHolder.getContext().setAuthentication(athentication);

            } catch (Exception e) {
                throw new BadCredentialsException("invalid token...");
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);

    }
}
