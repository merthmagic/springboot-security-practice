package org.xemi.poc.security.security;

import org.apache.shiro.authc.AuthenticationToken;
import org.xemi.poc.security.jwt.JwtUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationToken implements AuthenticationToken {

    private final String tokenString;

    @Override
    public Object getPrincipal() {
        return this.tokenString;
    }

    @Override
    public Object getCredentials() {
        return this.getPrincipal();
    }

    public boolean isValid() {
        return JwtUtils.verify(this.tokenString);
    }
}
