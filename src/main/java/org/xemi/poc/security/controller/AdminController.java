package org.xemi.poc.security.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xemi.poc.security.dto.LoginRequest;
import org.xemi.poc.security.dto.LoginResponse;
import org.xemi.poc.security.service.LoginService;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {

    private final LoginService loginService;

    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@NonNull @RequestBody LoginRequest request) {
        log.debug("LoginRequest=>", request.toString());

        LoginResponse response = loginService.login(request.getLoginName(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}
