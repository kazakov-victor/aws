package com.nixsolutions.clouds.vkazakov.aws.mapper;

import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.entity.User;
import com.nixsolutions.clouds.vkazakov.aws.util.DateMapperUtil;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {DateMapperUtil.class, RoleIdMapper.class,
    PhotoMapper.class, PasswordMapper.class})

public interface UserMapper extends EntityMapper<UserDto, User>{
     @Mapping(target = "roleId", source = "role")
     @Mapping(target = "photo", source = "photoLink")
     @Mapping(target = "password")
     UserDto toDto(User user);

     @Mapping(target = "password", qualifiedByName = {"PasswordChange", "PasswordEncode"})
     @InheritInverseConfiguration
     User toEntity(UserDto userDto);
}
