package org.xemi.poc.security.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xemi.poc.security.domain.Permission;
import org.xemi.poc.security.domain.Role;
import org.xemi.poc.security.domain.User;
import org.xemi.poc.security.repository.PermissionRepository;
import org.xemi.poc.security.repository.RoleRepository;
import org.xemi.poc.security.repository.UserRepository;
import org.xemi.poc.security.service.MembershipService;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class MembershipServiceImpl implements MembershipService {

    private final PermissionRepository permissionRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;


    @Override
    public List<Role> getUserRoles(@NonNull User user) {
        return null;
    }

    @Override
    public List<Role> getExplicitRoles(@NonNull User user) {
        return null;
    }

    @Override
    public List<Role> getImplicitRoles(@NonNull User user) {
        return null;
    }

    @Override
    public List<Permission> getPermissions(@NonNull User user) {
        return null;
    }

    @Override
    public List<Permission> getPermissionsByRole(@NonNull Role role) {
        return null;
    }

    @Override
    public User getUserByLoginName(@NonNull String loginName) {
        return userRepository.findByLoginName(loginName);
    }

    @Override
    public User addUser(@NonNull User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
        return userRepository.save(user);
    }
}
