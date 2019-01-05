package org.xemi.poc.security.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xemi.poc.security.domain.*;
import org.xemi.poc.security.events.AuditTrailEvent;
import org.xemi.poc.security.repository.*;
import org.xemi.poc.security.service.MembershipService;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class MembershipServiceImpl implements MembershipService {

    private final ApplicationContext applicationContext;

    private final PermissionRepository permissionRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final UserRoleRelationshipRepository userRoleRelationshipRepository;

    private final RolePermissionRelationshipRepository rolePermissionRelationshipRepository;

    @Override
    public List<Role> getUserRoles(@NonNull User user) {
        return roleRepository.fetchRolesByUserId(user.getId());
    }

    @Override
    public List<Role> getExplicitRoles(@NonNull User user) {
        return roleRepository.getAllByRoleType(RoleType.EXPLICIT);
    }

    @Override
    public List<Role> getImplicitRoles(@NonNull User user) {
        return roleRepository.getAllByRoleType(RoleType.IMPLICIT);
    }

    @Override
    public List<Permission> getPermissions(@NonNull User user) {
        return permissionRepository.fetchPermissionByUserId(user.getId());
    }

    @Override
    public List<Permission> getPermissionsByRole(@NonNull Role role) {
        return permissionRepository.fetchPermissionByRoleId(role.getRoleId());
    }

    @Override
    public Role getRoleById(@NonNull Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
    }

    @Override
    public User getUserByLoginName(@NonNull String loginName) {
        return userRepository.findByLoginName(loginName);
    }

    @Override
    public User getUserById(@NonNull Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Override
    @Transactional
    public User addUser(@NonNull User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        User saved = userRepository.save(user);
        applicationContext.publishEvent(
                new AuditTrailEvent(OperatorConstant.SYSTEM,
                        AuditEvent.ADD_USER,
                        AuditEventType.MEMBERSHIP_CHANGE,
                        "添加新用户，id=" + saved.getId()));
        return saved;
    }

    @Override
    @Transactional
    public Role addRole(@NonNull Role role) {
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public boolean deleteRole(@NonNull Role role) {
        if (userRoleRelationshipRepository.countAllByRoleId(role.getRoleId()) == 0) {
            roleRepository.delete(role);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Role updateRole(@NonNull Role role) {
        Role updated = roleRepository.save(role);
        return updated;
    }

    @Override
    public Permission getPermissionById(@NonNull Long permissionId) {
        return permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("权限不存在"));
    }

    @Override
    @Transactional
    public Permission addPermission(@NonNull Permission permission) {
        Permission saved = permissionRepository.save(permission);
        return saved;
    }

    @Override
    @Transactional
    public boolean deletePermission(@NonNull Permission permission) {

        if (rolePermissionRelationshipRepository
                .countAllByPermissionId(permission.getPermissionId()) == 0) {

            permissionRepository.delete(permission);
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public Permission updatePermission(@NonNull Permission permission) {
        Permission updated = permissionRepository.save(permission);
        return updated;
    }

    @Override
    @Transactional
    public void userAddRole(@NonNull User user, @NonNull Role role) {
        UserRoleRelationship userRoleRelationship = new UserRoleRelationship();
        userRoleRelationship.setRoleId(role.getRoleId());
        userRoleRelationship.setUserId(user.getId());
        userRoleRelationshipRepository.save(userRoleRelationship);
    }

    @Override
    @Transactional
    public void userAddRoles(@NonNull User user, @NonNull Iterable<Role> roles) {
        List<UserRoleRelationship> list = new ArrayList<>();
        for (Role role : roles) {
            UserRoleRelationship userRoleRelationship = new UserRoleRelationship();
            userRoleRelationship.setRoleId(role.getRoleId());
            userRoleRelationship.setUserId(user.getId());
            list.add(userRoleRelationship);
        }
        if (!list.isEmpty())
            userRoleRelationshipRepository.saveAll(list);
    }

    @Override
    @Transactional
    public void userRemoveRole(@NonNull User user, @NonNull Role role) {
        UserRoleRelationship candidate =
                userRoleRelationshipRepository
                        .findByUserIdAndRoleId(user.getId(), role.getRoleId());
        if (candidate != null)
            userRoleRelationshipRepository.delete(candidate);
    }

    @Override
    @Transactional
    public void userRemoveRoles(@NonNull User user, @NonNull Iterable<Role> roles) {
        List<UserRoleRelationship> candidateList = new ArrayList<>();
        for (Role role : roles) {
            UserRoleRelationship userRoleRelationship
                    = userRoleRelationshipRepository.findByUserIdAndRoleId(user.getId(), role.getRoleId());
            if (userRoleRelationship != null)
                candidateList.add(userRoleRelationship);
        }
        userRoleRelationshipRepository.deleteAll(candidateList);
    }

    @Override
    @Transactional
    public void roleAddPermission(@NonNull Role role, @NonNull Permission permission) {
        RolePermissionRelationship rolePermissionRelationship = new RolePermissionRelationship();
        rolePermissionRelationship.setRoleId(role.getRoleId());
        rolePermissionRelationship.setPermissionId(permission.getPermissionId());
        rolePermissionRelationshipRepository.save(rolePermissionRelationship);
    }

    @Override
    @Transactional
    public void roleAddPermissions(@NonNull Role role, @NonNull Iterable<Permission> permissions) {
        List<RolePermissionRelationship> list = new ArrayList<>();
        for (Permission permission : permissions) {
            RolePermissionRelationship rolePermissionRelationship = new RolePermissionRelationship();
            rolePermissionRelationship.setRoleId(role.getRoleId());
            rolePermissionRelationship.setPermissionId(permission.getPermissionId());
            list.add(rolePermissionRelationship);
        }
        if (!list.isEmpty())
            rolePermissionRelationshipRepository.saveAll(list);
    }

    @Override
    @Transactional
    public void roleRemovePermission(@NonNull Role role, @NonNull Permission permission) {
        RolePermissionRelationship candidate =
                rolePermissionRelationshipRepository
                        .findByRoleIdAndPermissionId(role.getRoleId(), permission.getPermissionId());
        if (candidate != null)
            rolePermissionRelationshipRepository.delete(candidate);
    }

    @Override
    @Transactional
    public void roleRemovePermission(@NonNull Role role, @NonNull Iterable<Permission> permissions) {
        List<RolePermissionRelationship> candidateList = new ArrayList<>();
        for (Permission permission : permissions) {
            RolePermissionRelationship rps =
                    rolePermissionRelationshipRepository
                            .findByRoleIdAndPermissionId(role.getRoleId(), permission.getPermissionId());
            if (rps != null)
                candidateList.add(rps);
        }
        rolePermissionRelationshipRepository.deleteAll(candidateList);
    }
}
