package com.nixsolutions.clouds.vkazakov.aws.service;


import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import java.util.List;

public interface RoleService {
    Role save(Role role);

    Role findByName(String name);

    Role findRoleById(Long id);

    List<Role> getAllRoles();

    void deleteById(Long id);
}
