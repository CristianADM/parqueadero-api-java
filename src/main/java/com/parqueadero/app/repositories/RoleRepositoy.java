package com.parqueadero.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parqueadero.app.models.RoleEntity;

@Repository
public interface RoleRepositoy extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);

}
