package org.xemi.poc.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.xemi.poc.security.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByLoginName(String loginName);
}
