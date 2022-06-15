package com.nixsolutions.clouds.vkazakov.aws.mapper;

import com.nixsolutions.clouds.vkazakov.aws.dto.RoleDto;
import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDto, Role>{
     RoleDto toDto(Role role);
@InheritInverseConfiguration
     Role toEntity(RoleDto roleDto);
}
