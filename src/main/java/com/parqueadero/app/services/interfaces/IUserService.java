package com.parqueadero.app.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.parqueadero.app.dtos.requests.UserRequest;
import com.parqueadero.app.dtos.responses.UserResponse;
import com.parqueadero.app.models.UserEntity;

public interface IUserService {

    List<UserResponse> findAllUsers();
    
    UserResponse createUser(UserRequest userRequest);

    UserResponse updateUser(Long idUser, UserRequest userRequest);

    UserEntity findUserById(Long id);
    
    UserEntity findUserByEmail(String email);

    UserEntity checkOptionalEmpty(Optional<UserEntity> userOptional);
}
