package com.nixsolutions.clouds.vkazakov.aws.repository;

import com.nixsolutions.clouds.vkazakov.aws.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);
    User findUserByEmail(String email);

}
