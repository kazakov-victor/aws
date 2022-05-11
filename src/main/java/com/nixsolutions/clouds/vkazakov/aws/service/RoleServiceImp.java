package com.nixsolutions.clouds.vkazakov.aws.service;

import com.nixsolutions.clouds.vkazakov.aws.dto.RoleDto;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import com.nixsolutions.clouds.vkazakov.aws.mapper.RoleMapper;
import com.nixsolutions.clouds.vkazakov.aws.repository.RoleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImp implements RoleService {
   private final RoleRepository repository;
   private final RoleMapper roleMapper;

   @Override
   public void save(Role Role) {
      repository.save(Role);
   }

   @Override
   public Role findByName(String name) {
      return repository.findByName(name);
   }

   @Override
   public Role findRoleById(Long id) {
      return repository.findById(id).orElse(new Role());
   }

   public ResponseEntity<RoleDto>  getUserByIdResponse(Long id) {
      Role role = findRoleById(id);
      if (role != null) {
         RoleDto roleDto = roleMapper.toDto(role);
         return ResponseEntity.ok(roleDto);
      }
      return ResponseEntity.notFound().build();
   }

   @Override
   public List<Role> getAllRoles() {
      return repository.findAll();
   }

}
