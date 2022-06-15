package com.nixsolutions.clouds.vkazakov.aws.repository;

import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

