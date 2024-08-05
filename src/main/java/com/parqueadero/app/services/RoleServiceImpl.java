package com.parqueadero.app.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.parqueadero.app.models.RoleEntity;
import com.parqueadero.app.repositories.RoleRepositoy;
import com.parqueadero.app.services.interfaces.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService {

    private RoleRepositoy roleRepositoy;

    public RoleServiceImpl(RoleRepositoy roleRepositoy) {
        this.roleRepositoy = roleRepositoy;
    }

    @Override
    public Optional<RoleEntity> findRoleEntityByName(String name) {
        return this.roleRepositoy.findByName(name);
    }
}