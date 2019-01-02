package org.xemi.poc.security.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.xemi.poc.security.domain.User;
import org.xemi.poc.security.service.MembershipService;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MiscController {


    private final MembershipService membershipService;

    @GetMapping(value = "/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping(value = "/error-401")
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Map<String, String> error401() {
        Map<String, String> ret = new HashMap<>();
        ret.put("status", "failed");
        ret.put("message", "尚未登录或AccessToken已过期，请重新登录");
        return ret;
    }

    @GetMapping(value = "/error-403")
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public Map<String, String> error403() {
        Map<String, String> ret = new HashMap<>();
        ret.put("status", "failed");
        ret.put("message", "没有足够的权限访问此资源");
        return ret;
    }

    @PostMapping(value = "/register")
    public User register() {
        User user = new User();
        user.setLoginName("test");
        user.setPassword("mypwd");
        return membershipService.addUser(user);
    }

    /**
     * 测试角色过滤是否生效
     */
    @GetMapping(value = "/greeting")
    @RequiresRoles(value = {"test"})
    public String greeting() {
        return "Hello World";
    }

    @GetMapping(value = "/test")
    @RequiresRoles(value = {"test"})
    @RequiresPermissions(value = {"perm1", "perm2"}, logical = Logical.OR)
    public String testPermission() {
        return "Permission test";
    }

    @GetMapping(value = "/test/{id}")
    public Map<String, String> testResource(@PathVariable Long id) {
        Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("test-perm:" + id + ":read");

        Map<String, String> ret = new LinkedHashMap<>();
        ret.put("test", new Date().toString());
        return ret;
    }
}


