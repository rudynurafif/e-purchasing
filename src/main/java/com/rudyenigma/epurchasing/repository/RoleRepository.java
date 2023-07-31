package com.rudyenigma.epurchasing.repository;

import com.rudyenigma.epurchasing.entity.Role;
import com.rudyenigma.epurchasing.entity.constant.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByRole(ERole role);

}
