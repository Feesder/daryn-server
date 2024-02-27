package com.boyapcky.bookworld.security;

import com.boyapcky.bookworld.entity.RoleEntity;
import com.boyapcky.bookworld.entity.UserEntity;
import com.boyapcky.bookworld.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Getter
    @Value("${jwt.token.lifespan.access-token}")
    private long accessTokenValidityInMilliseconds;

    @Getter
    @Value("${jwt.token.lifespan.refresh-token}")
    private long refreshTokenValidityInMilliseconds;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(UserEntity userEntity, Token token) {
        Claims claims = Jwts.claims().setSubject(userEntity.getUsername());
        claims.put("roles", getRoleNames(userEntity.getRoles()));

        Date now = new Date();
        Date validity = switch (token) {
            case ACCESS_TOKEN -> new Date(now.getTime() + accessTokenValidityInMilliseconds);
            case REFRESH_TOKEN -> new Date(now.getTime() + refreshTokenValidityInMilliseconds);
        };

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Jwt token is expired or invalid");
        }
    }

    private List<String> getRoleNames(List<RoleEntity> roles) {
        List<String> result = new ArrayList<>();

        roles.forEach(role -> {
           result.add(role.getName());
        });

        return result;
    }
}
