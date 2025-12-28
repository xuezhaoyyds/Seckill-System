package com.xz.login.controller;

import com.xz.login.model.login.LoginRequest;
import com.xz.login.model.login.LoginResponse;
import com.xz.login.model.register.RegisterRequest;
import com.xz.login.model.register.RegisterResponse;
import com.xz.login.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    
    @Autowired
    private AuthService authService;

    /**
     * 登录接口
     * @param loginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest,
                               HttpServletRequest request) {
        return authService.login(loginRequest, request);
    }

    /**
     * 注册接口
     * @param registerRequest
     * @return
     */
    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @GetMapping("/health")
    public String health() {
        return "Auth service is running";
    }

}