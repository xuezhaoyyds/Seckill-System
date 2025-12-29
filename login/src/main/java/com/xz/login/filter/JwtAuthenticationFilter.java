package com.xz.login.filter;

import com.xz.login.model.constants.CommonConstant;
import com.xz.login.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String jwt = parseJwt(request);
            
            if (jwt != null && jwtUtil.validateToken(jwt)) {
                String username = jwtUtil.getUsernameFromToken(jwt);
                Long userId = jwtUtil.getUserIdFromToken(jwt);
                String userType = jwtUtil.getUserTypeFromToken(jwt);
                
                logger.debug("JWT认证成功 - 用户: {}, ID: {}, 类型: {}", username, userId, userType);
                
                // 根据userType设置角色
                List<SimpleGrantedAuthority> authorities = getAuthorities(userType);
                
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            logger.warn("JWT Token已过期: {}", e.getMessage());
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            logger.warn("JWT Token格式错误: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("JWT Token为空或非法: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT认证失败: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        
        return null;
    }
    
    private List<SimpleGrantedAuthority> getAuthorities(String userType) {
        if (userType == null) {
            return Collections.emptyList();
        }
        
        switch (userType) {
            case CommonConstant.SUPER_ADMIN_CODE:
                return List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"), 
                             new SimpleGrantedAuthority("ROLE_ADMIN"),
                             new SimpleGrantedAuthority("ROLE_USER"));
            case CommonConstant.ADMIN_CODE:
                return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                             new SimpleGrantedAuthority("ROLE_USER"));
            case CommonConstant.NORMAL_USER_CODE:
            default:
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }
}