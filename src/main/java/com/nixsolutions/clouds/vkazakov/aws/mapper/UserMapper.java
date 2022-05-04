package com.nixsolutions.clouds.vkazakov.aws.mapper;

import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {DateMapper.class, RoleIdMapper.class, PhotoMapper.class})
public interface UserMapper extends EntityMapper<UserDto, User>{
     @Mapping(target = "roleId", source = "role")
     @Mapping(target = "photo", source = "photoLink")
     UserDto toDto(User user);
     @InheritInverseConfiguration
     User toEntity(UserDto userDto);
}
