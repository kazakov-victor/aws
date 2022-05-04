package com.nixsolutions.clouds.vkazakov.aws.service;

import com.nixsolutions.clouds.vkazakov.aws.entity.User;
import com.nixsolutions.clouds.vkazakov.aws.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found with -> username : " + username);
        }
        return UserPrinciple.build(user);
    }
}
