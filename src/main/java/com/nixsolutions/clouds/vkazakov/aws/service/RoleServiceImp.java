package com.nixsolutions.clouds.vkazakov.aws.service;


import com.nixsolutions.clouds.vkazakov.aws.entity.Role;
import com.nixsolutions.clouds.vkazakov.aws.repository.RoleRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImp implements RoleService {
   @Autowired
   RoleRepository repository;

   public RoleServiceImp(RoleRepository repository) {
      this.repository = repository;
   }

   @Override
   public Role save(Role Role) {
      return repository.save(Role);
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

   @Override
   public void deleteById(Long id) {
      repository.deleteById(id);
   }


}
