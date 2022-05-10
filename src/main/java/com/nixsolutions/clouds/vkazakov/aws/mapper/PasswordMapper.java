package com.nixsolutions.clouds.vkazakov.aws.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Named("PasswordChange")
public class PasswordMapper {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Named("PasswordEncode")
    public String encode(String password) {
        return  bCryptPasswordEncoder.encode(password);
    }

}
