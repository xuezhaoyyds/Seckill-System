package com.xz.login.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xz.login.mapper.UserMapper;
import com.xz.login.model.User;
import com.xz.login.model.login.LoginRequest;
import com.xz.login.model.login.LoginResponse;
import com.xz.login.model.register.RegisterRequest;
import com.xz.login.model.register.RegisterResponse;
import com.xz.login.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {


    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request) {
        return null;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        return null;
    }

}