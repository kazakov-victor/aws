package com.nixsolutions.clouds.vkazakov.aws.service;

import com.nixsolutions.clouds.vkazakov.aws.dto.RoleDto;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface RoleService {
    void save(Role role);

    Role findByName(String name);

    Role findRoleById(Long id);

    List<Role> getAllRoles();

    ResponseEntity<RoleDto> getUserByIdResponse(Long id);
}
