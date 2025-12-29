package com.xz.login.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * MyBatis Plus 自动填充处理器
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    
    /**
     * 插入时自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 填充 createTime（使用 Date 类型）
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        
        // 填充 updateTime（如果也需要）
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());


        this.strictInsertFill(metaObject, "lastLoginTime", LocalDateTime.class, LocalDateTime.now());


        // 如果有其他需要填充的字段，可以在这里添加
        // 比如：createBy（创建人）
        // this.strictInsertFill(metaObject, "createBy", String.class, getCurrentUser());
    }
    
    /**
     * 更新时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 填充 updateTime
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());

    }
}