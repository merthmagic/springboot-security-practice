package org.xemi.poc.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.xemi.poc.security.domain.Permission;

import java.util.List;

public interface PermissionRepository extends CrudRepository<Permission, Long> {

    @Query(value = "select p from User u " +
            "join UserRoleRelationship ur " +
            "on u.id = ur.userId " +
            "join RolePermissionRelationship rp " +
            "on ur.roleId = rp.roleId " +
            "left join Permission p " +
            "on rp.permissionId = p.permissionId " +
            "where u.id = :userId")
    List<Permission> fetchPermissionByUserId(@Param(value = "userId") Long userId);
}
