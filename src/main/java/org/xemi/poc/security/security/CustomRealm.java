package org.xemi.poc.security.security;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.xemi.poc.security.domain.User;
import org.xemi.poc.security.service.MembershipService;

import java.util.Objects;

/**
 * 自定义Realm
 */
@Slf4j
@Component
public class CustomRealm extends AuthorizingRealm {

    private final MembershipService membershipService;

    @Autowired
    public CustomRealm(MembershipService membershipService,
                       CustomCredentialsMatcher customCredentialsMatcher) {
        this.membershipService = membershipService;
        this.setCredentialsMatcher(customCredentialsMatcher);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole("admin");
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        if (token instanceof UsernamePasswordToken) {
            log.debug("登录操作：UsernamePasswordToken");


            UsernamePasswordToken upToken = (UsernamePasswordToken) token;

            String username = upToken.getUsername();
            char[] password = upToken.getPassword();
            if (username == null) {
                throw new AccountException("用户名不能为空");
            }

            User user = membershipService.getUserByLoginName(username);
            if (user == null)
                throw new AuthenticationException("用户名或密码错误");

            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, username);
            return info;
        } else if (token instanceof JwtAuthenticationToken) {
            //TODO: implement jwt auth business logical here
            log.debug("登录操作：JwtAuthenticationToken");
            //throw new UnsupportedOperationException("Not implemented");
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), "");
            return info;
        } else {
            throw new UnsupportedOperationException("Token not supported");
        }
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        //尝试同时支持两种token
        return (token instanceof UsernamePasswordToken || token instanceof JwtAuthenticationToken);
    }

    /*
    @Bean
    public Authorizer authorizer(MembershipService membershipService,
                                 CustomCredentialsMatcher customCredentialsMatcher) {
        CustomRealm customRealm = new CustomRealm(membershipService, customCredentialsMatcher);
        return customRealm;
    }
    */
}
