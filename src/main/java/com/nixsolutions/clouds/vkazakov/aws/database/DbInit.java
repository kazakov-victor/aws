package com.nixsolutions.clouds.vkazakov.aws.database;

import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import com.nixsolutions.clouds.vkazakov.aws.entity.User;
import com.nixsolutions.clouds.vkazakov.aws.service.RoleService;
import com.nixsolutions.clouds.vkazakov.aws.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;

/**
 * Inits database with default users
 */

@Component
public class DbInit implements InitializingBean {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Adds users in database
     */

    @Override
    public void afterPropertiesSet() {
        if (roleService.getAllRoles().size() == 0) {
            Role roleAdmin = new Role("ROLE_ADMIN");
            Role roleUser = new Role("ROLE_USER");
            roleService.save(roleAdmin);
            roleService.save(roleUser);
        }

        if (userService.findAll().size() == 0) {
            User userAdmin = new User("Adam", "Nilson", "admin",
                    "adam@gmail.com", bCryptPasswordEncoder.encode("admin"),
                    Date.valueOf("1990-12-01"),
                    roleService.findByName("ROLE_ADMIN"), "+380503332211", "");
            User user = new User("Sam", "Pierson", "user",
                    "sam@gmail.com", bCryptPasswordEncoder.encode("user"),
                    Date.valueOf("1980-10-21"),
                    roleService.findByName("ROLE_USER"), "+380503332222", "");
            userService.save(userAdmin);
            userService.save(user);
        }
    }

}
