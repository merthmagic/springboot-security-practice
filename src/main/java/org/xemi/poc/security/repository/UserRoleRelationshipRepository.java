package org.xemi.poc.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.xemi.poc.security.domain.UserRoleRelationship;

public interface UserRoleRelationshipRepository extends CrudRepository<UserRoleRelationship, Long> {

    int countAllByRoleId(Long roleId);

    UserRoleRelationship findByUserIdAndRoleId(Long userId, Long roleId);
}
