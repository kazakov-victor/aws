package com.nixsolutions.clouds.vkazakov.aws.mapper;

import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import com.nixsolutions.clouds.vkazakov.aws.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleIdMapper {
    private final RoleService roleService;

    public String map(Role role) {
        if (role == null) {
            return null;
        }
        return role.getId().toString();
    }

    public Role map(String roleId) {
        if (roleId == null) {
            return null;
        }
        return roleService.findRoleById(Long.parseLong(roleId));
    }
}
