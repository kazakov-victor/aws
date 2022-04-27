package com.nixsolutions.clouds.vkazakov.aws.service;

import com.nixsolutions.clouds.vkazakov.aws.entity.User;
import com.nixsolutions.clouds.vkazakov.aws.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserServiceImp implements UserService {
    @Autowired
    UserRepository repository;

    public UserServiceImp(UserRepository repository) {
        this.repository = repository;
    }

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
        return repository.findAll();
    }

    @Override
    public User save(User User) {
        return repository.save(User);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}

