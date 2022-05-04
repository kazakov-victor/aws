package com.nixsolutions.clouds.vkazakov.aws.service;

import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import com.nixsolutions.clouds.vkazakov.aws.repository.RoleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImp implements RoleService {
   private final RoleRepository repository;

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

   @Override
   public List<Role> getAllRoles() {
      return repository.findAll();
   }

}
