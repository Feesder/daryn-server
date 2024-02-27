package com.boyapcky.bookworld.exception;


import org.springframework.security.core.AuthenticationException;

import java.util.Arrays;

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
