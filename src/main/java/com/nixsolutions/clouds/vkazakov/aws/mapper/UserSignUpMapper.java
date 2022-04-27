package com.nixsolutions.clouds.vkazakov.aws.mapper;

import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserSignUpDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {DateMapper.class, RoleIdMapper.class,
PhotoMapper.class})
public interface UserSignUpMapper extends EntityMapper<UserDto, UserSignUpDTO>{
     @Mapping(target = "roleId", constant="2")
     @Mapping(target = "passwordAgain", source = "password")
     UserDto toDto(UserSignUpDTO userSignUpDTO);
}
