package com.parqueadero.app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.parqueadero.app.models.RoleEntity;

@Repository
public interface RoleRepositoy extends CrudRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);

}
