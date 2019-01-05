package org.xemi.poc.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.xemi.poc.security.domain.RolePermissionRelationship;

import java.util.List;

public interface RolePermissionRelationshipRepository
        extends CrudRepository<RolePermissionRelationship, Long> {

    List<RolePermissionRelationship> findAllByPermissionId(Long permissionId);

    int countAllByPermissionId(Long permissionId);

    RolePermissionRelationship findByRoleIdAndPermissionId(Long roleId,Long permissionId);
}
