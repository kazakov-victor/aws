package com.nixsolutions.clouds.vkazakov.aws.mapper;

import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import com.nixsolutions.clouds.vkazakov.aws.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleNameMapper {
    private final RoleService roleService;

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
