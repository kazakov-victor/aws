package com.nixsolutions.clouds.vkazakov.aws.service;

import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.entity.User;
import java.util.List;

public interface UserService {
    User findById(Long id);
    List<User> findAll();
    void save(User user);
    void saveUserDto(UserDto userDto);
    void deleteById(Long id);
    User findUserByUsername(String username);
    User findUserByEmail(String email);

}
