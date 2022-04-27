package com.nixsolutions.clouds.vkazakov.aws.mapper;

import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import com.nixsolutions.clouds.vkazakov.aws.service.RoleService;
import org.springframework.stereotype.Component;

@Component
public class RoleNameMapper {
    private RoleService roleService;

    public RoleNameMapper(RoleService roleService) {
        this.roleService = roleService;
    }

    public String map(Role role) {
        if (role == null) {
            return null;
        }
        return role.getName();
    }

    public Role map(String roleName) {
        if (roleName == null) {
            return null;
        }
        return roleService.findByName(roleName);
    }

}
