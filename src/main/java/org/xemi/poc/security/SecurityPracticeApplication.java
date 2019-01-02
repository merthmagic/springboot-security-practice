package org.xemi.poc.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ControllerAdvice
public class SecurityPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityPracticeApplication.class, args);
    }

    /**
     * 处理权限不足异常
     */
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Map<String, Object>> handleException(AuthorizationException e) {
        //这里可以返回404，github采用这个状态码
        log.debug("AuthorizationException was thrown", e);

        Map<String, Object> map = new HashMap<>();
        map.put("status", HttpStatus.FORBIDDEN.value());
        map.put("message", "没有权限访问此资源");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
    }

    /**
     * 处理登录失败的异常
     * IncorrectCredentialsException inherited from AuthenticationException
     */
    //@ExceptionHandler(value = {IncorrectCredentialsException.class, AuthenticationException.class})
    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<Map<String, Object>> handleIncorrectCredentialsException(AuthenticationException e) {
        log.debug("AuthenticationException was thrown", e);

        Map<String, Object> map = new HashMap<>();
        map.put("status", HttpStatus.UNAUTHORIZED.value());
        map.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }
}

