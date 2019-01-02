package org.xemi.poc.security.jwt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class JwtUtils {

    public static String signToken(JwtPrincipal jwtPrincipal) {
        throw new UnsupportedOperationException();
    }

    public static boolean verify(String token) {
        return true;
        //throw new UnsupportedOperationException();
    }
}
