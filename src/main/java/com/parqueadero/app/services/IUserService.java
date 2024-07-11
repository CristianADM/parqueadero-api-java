package com.parqueadero.app.services;

import java.util.List;

import com.parqueadero.app.dtos.requests.UserRequest;
import com.parqueadero.app.dtos.responses.UserResponse;

public interface IUserService {

    public List<UserResponse> findAllUsers();

    public UserResponse createUser(UserRequest userRequest);

    public UserResponse updateUser(Long idUser, UserRequest userRequest);
}
