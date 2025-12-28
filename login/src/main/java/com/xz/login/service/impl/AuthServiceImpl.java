package com.xz.login.service.impl;

import com.xz.login.model.login.LoginRequest;
import com.xz.login.model.login.LoginResponse;
import com.xz.login.model.register.RegisterRequest;
import com.xz.login.model.register.RegisterResponse;
import com.xz.login.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request) {
        return null;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        return null;
    }
}