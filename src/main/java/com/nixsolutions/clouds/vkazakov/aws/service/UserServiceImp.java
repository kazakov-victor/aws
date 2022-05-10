package com.nixsolutions.clouds.vkazakov.aws.service;

import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.entity.User;
import com.nixsolutions.clouds.vkazakov.aws.mapper.UserMapper;
import com.nixsolutions.clouds.vkazakov.aws.repository.UserRepository;
import com.nixsolutions.clouds.vkazakov.aws.validate.UserValidator;
import com.nixsolutions.clouds.vkazakov.aws.validate.XSSCleaner;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImp implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final XSSCleaner xssCleaner;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final S3BucketService s3BucketService;

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElse(new User());
    }

    @Override
    public User findUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    private void save(User User) {
        repository.save(User);
    }

    public void saveUserDto(UserDto userDto) {
        userDto = checkUserDto(userDto);
        User user = userMapper.toEntity(userDto);
        save(user);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public ResponseEntity<String> getEditResponse(UserDto userDto, Long oldUserId) {
        userDto = checkUserDto(userDto);
        User userOld = findById(oldUserId);
        User userNew = setUpNewUserFields(userDto, oldUserId, userOld);
        s3BucketService.deleteFile(userOld.getPhotoLink());
        save(userNew);
        return ResponseEntity.status(201).body("Entity was update!");
    }

    public ResponseEntity<UserDto> getFindUserResponse(String username) {
        String usernameClean = xssCleaner.cleanValue(username);
        User user = findUserByUsername(usernameClean);
        if (user != null) {
            return ResponseEntity.ok(userMapper.toDto(user));
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<String> getDeleteResponse(Long id) {
        if (findById(id) != null) {
            deleteById(id);
            return ResponseEntity.ok("User was delete!");
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<UserDto> getUserByIdResponse(Long id) {
        User user = findById(id);
        if (user != null) {
            UserDto userDto = userMapper.toDto(user);
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }

    private UserDto checkUserDto(UserDto userDto) {
        userDto = xssCleaner.cleanFields(userDto);
        UserValidator.validate(userDto);
        return userDto;
    }

    private User setUpNewUserFields(UserDto userDto, Long oldUserId, User userOld) {
        User userNew = userMapper.toEntity(userDto);
        userNew.setId(oldUserId);
        userNew.setUsername(userOld.getUsername());
        setUserNewPassword(userOld, userNew);
        return userNew;
    }

    private void setUserNewPassword(User userOld, User userNew) {
        if (!userNew.getPassword().equals("")) {
            userNew.setPassword(bCryptPasswordEncoder.encode(userNew.getPassword()));
        } else {
            userNew.setPassword(userOld.getPassword());
        }
    }
}

