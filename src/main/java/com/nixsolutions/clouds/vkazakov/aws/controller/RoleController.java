package com.nixsolutions.clouds.vkazakov.aws.controller;

import com.nixsolutions.clouds.vkazakov.aws.dto.RoleDto;
import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import com.nixsolutions.clouds.vkazakov.aws.mapper.RoleMapper;
import com.nixsolutions.clouds.vkazakov.aws.service.RoleService;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class RoleController {
    private final RoleService roleService;
    private final RoleMapper roleMapper;

    /**
     * Returns list of all roles
     */
    @CrossOrigin
    @GetMapping("/role")
    public @ResponseBody
    List<RoleDto> listRoles() {
        List<Role> roles = roleService.getAllRoles();
        return roleMapper.toDto(roles);
    }

    /**
     * Finds role by id
     */
    @CrossOrigin
    @GetMapping("role/{id}")
    public ResponseEntity<RoleDto> findRoleById(@PathVariable Long id) {
        if (isIdWrong(id)) {
            return ResponseEntity.status(400).build();
        }
        Role role = roleService.findRoleById(id);
        if (role != null) {
            RoleDto roleDto = roleMapper.toDto(role);
            return ResponseEntity.ok(roleDto);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Checks is id correct
     * If it is wrong return true or false in another case
     */
    private boolean isIdWrong(Long id) {
        String ID_REGEX = "^[0-9]+$";
        Pattern pattern = Pattern.compile(ID_REGEX);
        Matcher matcher = pattern.matcher(String.valueOf(id));
        Role role = roleService.findRoleById(id);
        return id == null || !matcher.find() || role == null;
    }
}


