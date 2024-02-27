package com.boyapcky.bookworld.controller;

import com.boyapcky.bookworld.exception.WrongPasswordException;
import com.boyapcky.bookworld.model.authorization.*;
import com.boyapcky.bookworld.service.AuthService;
import com.boyapcky.bookworld.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
@Slf4j
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/register")
    public ResponseEntity register(@RequestBody RegisterRequest request, HttpServletResponse http) {
        try {
            RegisterResponse response = authService.register(request);
            authService.setToken(response.getRefreshToken(), http);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody LoginRequest request, HttpServletResponse http) {
        try {
            LoginResponse response = authService.login(request);
            authService.setToken(response.getRefreshToken(), http);
            return ResponseEntity.ok(response);
        } catch (WrongPasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @PostMapping(value = "/logout")
    public ResponseEntity logout(@RequestBody LoginRequest request, HttpServletResponse http) {
        try {
            authService.clearToken(http);
            return ResponseEntity.ok("successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @GetMapping(value = "/refresh")
    public ResponseEntity refresh(@CookieValue(name = "refresh_token") Cookie cookie, HttpServletResponse http) {
        try {
            JwtResponse response = authService.getNewToken(cookie.getValue());
            authService.setToken(response.getRefreshToken(), http);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }
}
