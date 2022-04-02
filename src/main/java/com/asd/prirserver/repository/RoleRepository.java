package com.asd.prirserver.repository;

import com.asd.prirserver.model.ERole;
import com.asd.prirserver.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>
{
    Optional<Role> findByRoleName(ERole name);
}
