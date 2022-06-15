package com.nixsolutions.clouds.vkazakov.aws.database;

import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import com.nixsolutions.clouds.vkazakov.aws.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Inits database with default users
 */
@RequiredArgsConstructor
@Component
public class DbInit implements InitializingBean {
    private final RoleService roleService;
    private final static String ROLE_ADMIN = "ROLE_ADMIN";
    private final static String ROLE_USER = "ROLE_USER";

    @Override
    public void afterPropertiesSet() {
        if (roleService.getAllRoles().size() == 0) {
            Role roleAdmin = new Role(ROLE_ADMIN);
            Role roleUser = new Role(ROLE_USER);
            roleService.save(roleAdmin);
            roleService.save(roleUser);
        }
    }

}
