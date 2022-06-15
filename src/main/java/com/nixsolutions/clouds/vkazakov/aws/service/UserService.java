package com.nixsolutions.clouds.vkazakov.aws.service;

import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.entity.User;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface UserService {
    User findById(Long id);
    List<User> findAll();
    void saveUserDto(UserDto userDto);
    void deleteById(Long id);
    User findUserByUsername(String username);
    ResponseEntity<String> getEditResponse(UserDto userDto, Long id);
    ResponseEntity<UserDto> getFindUserResponse(String username);
    ResponseEntity<String> getDeleteResponse(Long id);
    ResponseEntity<UserDto> getUserByIdResponse(Long id);
}
