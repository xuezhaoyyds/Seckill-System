package com.xz.login.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户类型枚举
 * 1-普通用户，2-管理员，3-超级管理员
 */
@Getter
public enum UserTypeEnum {
    
    /**
     * 普通用户
     */
    NORMAL_USER(1, "普通用户"),
    
    /**
     * 管理员
     */
    ADMIN(2, "管理员"),
    
    /**
     * 超级管理员
     */
    SUPER_ADMIN(3, "超级管理员");
    
    /**
     * 类型编码
     */
    private final Integer code;
    
    /**
     * 类型描述
     */
    private final String description;
    
    UserTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 根据code获取枚举
     * @param code 类型编码
     * @return 对应的枚举，如果找不到返回null
     */
    public static UserTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * 根据code获取描述
     * @param code 类型编码
     * @return 类型描述，如果找不到返回"未知类型"
     */
    public static String getDescriptionByCode(Integer code) {
        UserTypeEnum type = getByCode(code);
        return type != null ? type.getDescription() : "未知类型";
    }
    
    /**
     * 判断code是否存在
     * @param code 类型编码
     * @return 是否存在
     */
    public static boolean contains(Integer code) {
        return getByCode(code) != null;
    }
    
    /**
     * 判断是否为管理员（包括管理员和超级管理员）
     * @param code 类型编码
     * @return 是否为管理员
     */
    public static boolean isAdmin(Integer code) {
        UserTypeEnum type = getByCode(code);
        return type != null && (type == ADMIN || type == SUPER_ADMIN);
    }
    
    /**
     * 判断是否为超级管理员
     * @param code 类型编码
     * @return 是否为超级管理员
     */
    public static boolean isSuperAdmin(Integer code) {
        UserTypeEnum type = getByCode(code);
        return type != null && type == SUPER_ADMIN;
    }
    
    /**
     * 获取所有用户类型编码
     * @return 编码列表
     */
    public static List<Integer> getAllCodes() {
        return Arrays.stream(values())
                     .map(UserTypeEnum::getCode)
                     .collect(Collectors.toList());
    }
    
    /**
     * 获取所有用户类型描述
     * @return 描述列表
     */
    public static List<String> getAllDescriptions() {
        return Arrays.stream(values())
                     .map(UserTypeEnum::getDescription)
                     .collect(Collectors.toList());
    }
    
    /**
     * 获取枚举的Map表示（code -> description）
     * @return Map
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new LinkedHashMap<>();
        for (UserTypeEnum type : values()) {
            map.put(type.getCode(), type.getDescription());
        }
        return map;
    }
}