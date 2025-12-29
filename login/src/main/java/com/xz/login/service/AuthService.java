package com.xz.login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xz.login.model.User;
import com.xz.login.model.login.LoginRequest;
import com.xz.login.model.login.LoginResponse;
import com.xz.login.model.register.RegisterRequest;
import com.xz.login.model.register.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService extends IService<User> {

    LoginResponse login(LoginRequest loginRequest);

    RegisterResponse register(RegisterRequest registerRequest);
}
