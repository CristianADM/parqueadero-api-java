package com.parqueadero.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.parqueadero.app.models.RoleEntity;

public interface RoleRepositoy extends CrudRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);

}
