package com.xz.login.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RefreshScope
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    //默认24h
    @Value("${jwt.expiration:86400000}")
    private long expiration;

    //生成token
    public String generateToken(String username, Long userId, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType);
        claims.put("username", username);

        return Jwts.builder()
                //声明
                .setClaims(claims)
                //主题
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //将字符串密钥转换为 HMAC SHA 密钥
    public SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    //验证token有效性
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (SecurityException e) {
            logger.error("JWT签名验证失败: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("JWT格式错误: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT已过期: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT不支持: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT参数错误: {}", e.getMessage());
        }
        return false;
    }

    //解析token，获取声明内容
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //查看token是否过期
    public boolean isTokenExpired(String token) {
        final Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return getClaimsFromToken(token).get("userId", Long.class);
    }

    public String getUserTypeFromToken(String token) {
        return getClaimsFromToken(token).get("userType", String.class);
    }


}
