package org.xemi.poc.security.service;

import org.xemi.poc.security.domain.Permission;
import org.xemi.poc.security.domain.Role;
import org.xemi.poc.security.domain.User;

import java.util.List;

/**
 * 用户，角色，权限相关服务，table gateway模式实现
 */
public interface MembershipService {
    /**
     * 添加用户
     * @param user
     * @return
     */
    User addUser(User user);

    /**
     * 根据用户名获取用户信息
     * @param loginName
     * @return
     */
    User getUserByLoginName(String loginName);

    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     */
    User getUserById(Long userId);

    /**
     * 根据用户获取角色
     * @param user
     * @return
     */
    List<Role> getUserRoles(User user);

    /**
     * 获取用户的explicit roles
     * @param user
     * @return
     */
    List<Role> getExplicitRoles(User user);

    /**
     * 获取用户implicit roles
     * @param user
     * @return
     */
    List<Role> getImplicitRoles(User user);

    /**
     * 获取用户权限
     * @param user
     * @return
     */
    List<Permission> getPermissions(User user);

    /**
     * 获取角色拥有的权限，拥有权限的角色属于explicit role
     * @param role
     * @return
     */
    List<Permission> getPermissionsByRole(Role role);

    /**
     * 根据id获取角色信息
     * @param roleId
     * @return
     */
    Role getRoleById(Long roleId);

    /**
     * 添加角色
     * @param role
     * @return
     */
    Role addRole(Role role);

    /**
     *
     * @param role
     * @return
     */
    boolean deleteRole(Role role);

    /**
     * 更新角色
     * @param role
     */
    Role updateRole(Role role);

    /**
     * 根据id获取权限信息
     * @param permissionId
     * @return
     */
    Permission getPermissionById(Long permissionId);

    /**
     * 添加权限
     * @param permission
     * @return
     */
    Permission addPermission(Permission permission);

    /**
     * 删除权限
     * @param permission
     * @return 移除成功true，反之false，如果权限已经添加到角色，无法删除
     */
    boolean deletePermission(Permission permission);

    /**
     * 更新权限
     * @param permission
     * @return
     */
    Permission updatePermission(Permission permission);


    /**
     * 用户增加角色
     * @param user
     * @param role
     */
    void userAddRole(User user,Role role);

    /**
     * 用户添加角色
     * @param user
     * @param roles
     */
    void userAddRoles(User user,Iterable<Role> roles);

    /**
     * 用户移除角色
     * @param user
     * @param role
     */
    void userRemoveRole(User user,Role role);

    /**
     * 用户移除角色
     * @param user
     * @param roles
     */
    void userRemoveRoles(User user,Iterable<Role> roles);

    /**
     * 角色添加权限
     * @param role
     * @param permission
     */
    void roleAddPermission(Role role,Permission permission);

    /**
     * 角色添加权限
     * @param role
     * @param permissions
     */
    void roleAddPermissions(Role role,Iterable<Permission> permissions);

    /**
     * 角色移除权限
     * @param role
     * @param permission
     */
    void roleRemovePermission(Role role,Permission permission);

    /**
     * 角色移除权限
     * @param role
     * @param permissions
     */
    void roleRemovePermission(Role role,Iterable<Permission> permissions);
}
