package com.xz.login.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户表(Users)实体类
 *
 * @author makejava
 * @since 2025-12-28 23:13:45
 */
@Data
@TableName("users")
public class User implements Serializable {
    private static final long serialVersionUID = -14141543786455517L;
    /**
     * 用户ID
     */
    @TableId(type= IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 加密后的密码
     */
    private String passwordHash;
    /**
     * 密码盐值
     */
    private String salt;
    /**
     * 状态：0-禁用，1-启用，2-锁定
     */
    private String status;
    /**
     * 用户类型：1-普通用户，2-管理员，3-超级管理员
     */
    private String userType;
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 连续登录失败次数
     */
    private Integer loginAttempts;
    /**
     * 锁定直到时间
     */
    private LocalDateTime lockUntilTime;
    /**
     * 密码最后修改时间
     */
    private LocalDateTime passwordChangedTime;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像URL
     */
    private String avatar;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 更新人
     */
    private String updateBy;


}

