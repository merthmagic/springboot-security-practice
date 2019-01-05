package org.xemi.poc.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.xemi.poc.security.domain.Role;
import org.xemi.poc.security.domain.RoleType;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Long> {

    @Query(value = "select r from Role r join UserRoleRelationship ur on r.roleId = ur.roleId " +
            " where  ur.userId= :userId")
    List<Role> fetchRolesByUserId(@Param("userId") Long userId);

    List<Role> getAllByRoleType(RoleType roleType);
}
