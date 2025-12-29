package com.xz.login.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordValidator {
    
    /**
     * 验证密码强度
     * @param password 密码
     * @return 错误信息，如果返回null表示密码强度足够
     */
    public String validatePasswordStrength(String password) {
        if (password == null || password.length() < 6) {
            return "密码长度至少6位";
        }
        
        if (password.length() > 20) {
            return "密码长度不能超过20位";
        }
        
        // 检查是否包含数字
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            return "密码必须包含数字";
        }
        
        // 检查是否包含字母
        if (!Pattern.compile("[a-zA-Z]").matcher(password).find()) {
            return "密码必须包含字母";
        }

        return null;
    }
    
    /**
     * 检查密码是否过于简单（常见弱密码）
     */
    public boolean isWeakPassword(String password) {
        String[] weakPasswords = {
            "123456", "password", "12345678", "qwerty", "abc123",
            "1234567", "111111", "1234567890", "123123", "000000"
        };
        
        for (String weak : weakPasswords) {
            if (weak.equalsIgnoreCase(password)) {
                return true;
            }
        }
        
        // 检查是否是连续数字
        if (Pattern.compile("^\\d+$").matcher(password).matches()) {
            return isSequentialNumbers(password);
        }
        
        return false;
    }
    
    private boolean isSequentialNumbers(String password) {
        // 检查是否是连续数字如123456, 654321等
        boolean ascending = true;
        boolean descending = true;
        
        for (int i = 1; i < password.length(); i++) {
            int current = Character.getNumericValue(password.charAt(i));
            int previous = Character.getNumericValue(password.charAt(i - 1));
            
            if (current != previous + 1) {
                ascending = false;
            }
            if (current != previous - 1) {
                descending = false;
            }
        }
        
        return ascending || descending;
    }
}