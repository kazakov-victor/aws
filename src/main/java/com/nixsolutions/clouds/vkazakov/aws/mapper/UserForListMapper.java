package com.nixsolutions.clouds.vkazakov.aws.mapper;

import com.nixsolutions.clouds.vkazakov.aws.dto.UserForListDto;
import com.nixsolutions.clouds.vkazakov.aws.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        uses = {DateForListMapper.class, RoleNameMapper.class})
public interface UserForListMapper extends EntityMapper<UserForListDto, User>{
     @Mapping(target = "age", source = "birthdate")
     @Mapping(target = "roleName", source = "role")
     UserForListDto toDto(User user);
}
