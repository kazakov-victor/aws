package com.nixsolutions.clouds.vkazakov.aws.controller;

import com.nixsolutions.clouds.vkazakov.aws.dto.RoleDto;
import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import com.nixsolutions.clouds.vkazakov.aws.mapper.RoleMapper;
import com.nixsolutions.clouds.vkazakov.aws.service.RoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RestController("/role")
public class RoleController {
    private final RoleService roleService;
    private final RoleMapper roleMapper;

    /**
     * Returns list of all roles
     */
    @CrossOrigin
    @GetMapping("/")
    public @ResponseBody
    List<RoleDto> listRoles() {
        List<Role> roles = roleService.getAllRoles();
        return roleMapper.toDto(roles);
    }

    /**
     * Finds role by id
     */
    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> findRoleById(@PathVariable Long id) {
        return roleService.getUserByIdResponse(id);
    }

}


