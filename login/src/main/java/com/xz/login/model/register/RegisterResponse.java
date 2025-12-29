package com.xz.login.model.register;

import lombok.Data;

@Data
public class RegisterResponse {
    private boolean success;
    private String message;
    private UserInfo userInfo;
    
    // 构造方法
    public RegisterResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public RegisterResponse(boolean success, String message, UserInfo userInfo) {
        this.success = success;
        this.message = message;
        this.userInfo = userInfo;
    }
    
    // 用户信息内部类
    @Data
    public static class UserInfo {
        private Long id;
        private String username;
        private String nickname;
        private String email;
        private String phone;
        private String userType;

    }
}