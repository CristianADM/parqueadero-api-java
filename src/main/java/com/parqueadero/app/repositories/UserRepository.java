package com.parqueadero.app.repositories;

import org.springframework.data.repository.CrudRepository;

import com.parqueadero.app.models.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

}
