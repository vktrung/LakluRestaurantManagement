package com.laklu.pos.repositories;

import com.laklu.pos.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleById(Integer id);
}
