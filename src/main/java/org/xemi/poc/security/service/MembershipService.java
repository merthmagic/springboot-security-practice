package org.xemi.poc.security.service;

import org.xemi.poc.security.domain.Permission;
import org.xemi.poc.security.domain.Role;
import org.xemi.poc.security.domain.User;

import java.util.List;

public interface MembershipService {

    List<Role> getUserRoles(User user);

    List<Role> getExplicitRoles(User user);

    List<Role> getImplicitRoles(User user);

    List<Permission> getPermissions(User user);

    List<Permission> getPermissionsByRole(Role role);

    User getUserByLoginName(String loginName);

    User addUser(User user);
}
