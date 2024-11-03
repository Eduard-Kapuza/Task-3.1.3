package com.kapuza.springSecurity.services.roleService;

import com.kapuza.springSecurity.models.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();

    Optional<Role> findByRoleName(String roleName);
}