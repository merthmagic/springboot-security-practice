package org.xemi.poc.security.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * jBcrypt实现的CredentialsMatcher
 */
@Component
public class CustomCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        if (token instanceof UsernamePasswordToken) {
            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
            String hashed = info.getCredentials().toString();
            try {
                return BCrypt.checkpw(new String(usernamePasswordToken.getPassword()), hashed);
            } catch (IllegalArgumentException e) {
                throw new AuthenticationException("Credentials Invalid", e);
            } catch (Throwable e) {
                throw new AuthenticationException(e);
            }
        } else if (token instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) token;
            return jwtAuthenticationToken.isValid();
        } else {
            throw new UnsupportedOperationException("Token not supported");
        }
    }
}
