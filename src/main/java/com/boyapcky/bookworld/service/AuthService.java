package com.boyapcky.bookworld.service;

import com.boyapcky.bookworld.entity.RoleEntity;
import com.boyapcky.bookworld.entity.Status;
import com.boyapcky.bookworld.entity.UserEntity;
import com.boyapcky.bookworld.exception.WrongPasswordException;
import com.boyapcky.bookworld.model.authorization.*;
import com.boyapcky.bookworld.repository.RoleRepository;
import com.boyapcky.bookworld.repository.UserRepository;
import com.boyapcky.bookworld.security.JwtTokenProvider;
import com.boyapcky.bookworld.security.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public RegisterResponse register(RegisterRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(request.getPassword());
        userEntity.setEmail(request.getEmail());

        RoleEntity roleEntity = roleRepository.findByName("ROLE_USER");
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity);

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(roles);
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setCreated(new Date());
        userEntity.setUpdated(new Date());

        UserEntity registeredUser = userRepository.save(userEntity);
        log.info("IN register - user: {} successfully registered", registeredUser);

        String refreshToken = jwtTokenProvider.createToken(userEntity, Token.REFRESH_TOKEN);
        String accessToken = jwtTokenProvider.createToken(userEntity, Token.REFRESH_TOKEN);

        return RegisterResponse.toModel(
                userEntity,
                refreshToken,
                accessToken
        );
    }

    public LoginResponse login(LoginRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new WrongPasswordException("Incorrect password");
        }
        UserEntity userEntity = userRepository.findByUsername(request.getUsername());

        if (userEntity == null) {
            throw new Exception("User with username " + request.getUsername() + " not found");
        }

        String refreshToken = jwtTokenProvider.createToken(userEntity, Token.REFRESH_TOKEN);
        String accessToken = jwtTokenProvider.createToken(userEntity, Token.ACCESS_TOKEN);

        return LoginResponse.toModel(
                userEntity,
                refreshToken,
                accessToken
        );
    }

    public JwtResponse getNewToken(String token) {
        String user = jwtTokenProvider.getUsername(token);
        UserEntity userEntity = userRepository.findByUsername(user);
        String accessToken = jwtTokenProvider.createToken(userEntity, Token.ACCESS_TOKEN);
        String refreshToken = jwtTokenProvider.createToken(userEntity, Token.REFRESH_TOKEN);

        return JwtResponse.toModel(
                userEntity,
                refreshToken,
                accessToken
        );
    }

    public void setToken(String token, HttpServletResponse http) {
        Cookie cookie = new Cookie("refresh_token", token);
        cookie.setMaxAge((int) jwtTokenProvider.getRefreshTokenValidityInMilliseconds());
        http.addCookie(cookie);
    }

    public void clearToken(HttpServletResponse http) {
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        http.addCookie(cookie);
    }
}
