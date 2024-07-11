package com.parqueadero.app.services;

import com.parqueadero.app.dtos.requests.UserRequest;
import com.parqueadero.app.dtos.responses.UserResponse;

public interface IUserService {

    public UserResponse createUser(UserRequest userRequest);

    public UserResponse updateUser(Long idUser, UserRequest userRequest);
}
