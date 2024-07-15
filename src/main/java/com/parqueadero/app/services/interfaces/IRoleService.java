package com.parqueadero.app.services.interfaces;

import java.util.Optional;

import com.parqueadero.app.models.RoleEntity;

public interface IRoleService {

    public Optional<RoleEntity> findRoleEntityByName(String name);
}
