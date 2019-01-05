package org.xemi.poc.security.domain;

public abstract class AuditEvent {

    public static final String ADD_USER = "添加用户";

    public static final String BAN_USER = "禁用用户";

    public static final String USER_ADD_ROLE = "用户添加角色";

    public static final String USER_REMOVE_ROLE = "用户移除角色";

    public static final String ROLE_ADD_PERMISSION="角色添加权限";

    public static final String ROLE_REMOVE_PERMISSION="角色移除权限";

    public static final String USER_LOGIN_FAIL = "用户登录失败";
}
