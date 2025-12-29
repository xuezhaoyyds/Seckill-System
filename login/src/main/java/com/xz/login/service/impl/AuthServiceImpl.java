package com.xz.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xz.login.mapper.UserMapper;
import com.xz.login.model.User;
import com.xz.login.model.constants.CommonConstant;
import com.xz.login.model.login.LoginRequest;
import com.xz.login.model.login.LoginResponse;
import com.xz.login.model.register.RegisterRequest;
import com.xz.login.model.register.RegisterResponse;
import com.xz.login.service.AuthService;
import com.xz.login.utils.JwtUtil;
import com.xz.login.utils.PasswordUtil;
import com.xz.login.utils.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordValidator passwordValidator;

    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 30;


    @Transactional
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return new LoginResponse(false, "该用户为注册");
        }
        //检查状态
        if (user.getStatus().equals(CommonConstant.USER_STATUS_DISABLE)) {
            return new LoginResponse(false, "用户已被禁用");
        }

        // 检查账户是否被锁定
        if (user.getLockUntilTime() != null && user.getLockUntilTime().isAfter(LocalDateTime.now())) {
            return new LoginResponse(false, "账户已被锁定，请稍后再试");
        }

        //检查密码
        if (!passwordUtil.verifyPassword(password, user.getPasswordHash())) {
            handleLoginFailure(user);
            int remainingAttempts = MAX_LOGIN_ATTEMPTS - user.getLoginAttempts();
            return new LoginResponse(false,
                    String.format("用户名或密码错误，剩余尝试次数：%d", remainingAttempts));
        }
        //登录成功
        handleLoginSuccess(user);

        //生成token
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getUserType());

        // 构建用户信息
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setEmail(user.getEmail());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setUserType(user.getUserType());

        return new LoginResponse(true, "登录成功", token, userInfo);

    }

    //登录失败后操作
    private void handleLoginFailure(User user) {
        int newAttempts = user.getLoginAttempts() + 1;

        if (newAttempts >= MAX_LOGIN_ATTEMPTS) {
            user.setLockUntilTime(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
            user.setLoginAttempts(0);
        } else {
            user.setLoginAttempts(newAttempts);
        }
        userMapper.updateById(user);
    }

    //登录成功后操作
    private void handleLoginSuccess(User user) {
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);
    }

    @Transactional
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        // 验证确认密码
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return new RegisterResponse(false, "两次输入的密码不一致");
        }

        // 验证密码强度
        String passwordStrengthError = passwordValidator.validatePasswordStrength(registerRequest.getPassword());
        if (passwordStrengthError != null) {
            return new RegisterResponse(false, passwordStrengthError);
        }

        // 检查是否为弱密码
        if (passwordValidator.isWeakPassword(registerRequest.getPassword())) {
            return new RegisterResponse(false, "密码过于简单，请使用更复杂的密码");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", registerRequest.getEmail())
                .or().eq("username", registerRequest.getUsername())
                .or().eq("phone", registerRequest.getPhone());
        boolean exists = userMapper.exists(queryWrapper);
        if (exists) {
            return new RegisterResponse(false, "用户名或邮箱或手机号已存在");
        }

        try {
            // 生成盐值和加密密码 - 更新为使用BCrypt
            String salt = passwordUtil.generateSalt();
            String passwordHash = passwordUtil.hashPassword(registerRequest.getPassword());

            // 创建用户对象
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPasswordHash(passwordHash);
            user.setSalt(salt);
            user.setEmail(registerRequest.getEmail());
            user.setPhone(registerRequest.getPhone());
            user.setNickname(registerRequest.getNickname() != null ?
                    registerRequest.getNickname() : registerRequest.getUsername());
            // 普通用户
            user.setUserType(CommonConstant.NORMAL_USER_CODE);
            user.setStatus(CommonConstant.USER_STATUS_ENABLE);
            user.setPasswordChangedTime(LocalDateTime.now());
            //保存用户
            save(user);


            // 构建响应数据
            RegisterResponse.UserInfo userInfo = new RegisterResponse.UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfo.setNickname(user.getNickname());
            userInfo.setEmail(user.getEmail());
            userInfo.setPhone(user.getPhone());
            userInfo.setUserType(user.getUserType());

            // 记录注册日志（可选）
            //log.info("用户注册成功: {}, ID: {}", savedUser.getUsername(), savedUser.getId());

            return new RegisterResponse(true, "注册成功", userInfo);

        } catch (Exception e) {
            throw new RuntimeException("注册失败，请稍后重试", e);
        }

    }

}