package com.parqueadero.app.repositories;

import org.springframework.data.repository.CrudRepository;

import com.parqueadero.app.models.RoleEntity;

public interface RoleRepositoy extends CrudRepository<RoleEntity, Long> {

}
