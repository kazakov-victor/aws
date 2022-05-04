package com.nixsolutions.clouds.vkazakov.aws.service;

import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.entity.User;
import com.nixsolutions.clouds.vkazakov.aws.mapper.UserMapper;
import com.nixsolutions.clouds.vkazakov.aws.repository.UserRepository;
import com.nixsolutions.clouds.vkazakov.aws.validate.UserValidator;
import com.nixsolutions.clouds.vkazakov.aws.validate.XSSCleaner;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElse(new User());
    }

    @Override
    public User findUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return repository.findUserByEmail(email);
    }

    @Override
    public List<User> findAll() {
        List<User> users = repository.findAll();
        for (User user : users) {
            user.setPassword("");
        }
        return users;
    }

    @Override
    public void save(User User) {
        repository.save(User);
    }

    public void saveUserDto(UserDto userDto) {
        userDto = xssCleaner.cleanFields(userDto);
        Map<String, String> errors = UserValidator.validate(userDto);

        if (errors.isEmpty()) {
            User user = userMapper.toEntity(userDto);
            String passNew = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(passNew);
            save(user);
        }
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}

