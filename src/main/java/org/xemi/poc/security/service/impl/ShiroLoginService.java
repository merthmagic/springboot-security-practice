package org.xemi.poc.security.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xemi.poc.security.dto.LoginResponse;
import org.xemi.poc.security.service.LoginService;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ShiroLoginService implements LoginService {

    @Override
    public LoginResponse login(String loginName, String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
        LoginResponse response = new LoginResponse();

        subject.login(token);
        response.setAccessToken("access-token");
        response.setRefreshToken("refresh-token");
        response.setExpireAt(7200L);
        response.setType("JWT");

        return response;
    }
}
