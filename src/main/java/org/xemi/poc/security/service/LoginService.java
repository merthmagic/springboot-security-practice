package org.xemi.poc.security.service;

import org.xemi.poc.security.dto.LoginResponse;

public interface LoginService {

    LoginResponse login(String loginName,String password);
}
