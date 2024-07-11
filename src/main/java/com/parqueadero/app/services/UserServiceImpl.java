package com.parqueadero.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.parqueadero.app.dtos.requests.UserRequest;
import com.parqueadero.app.dtos.responses.UserResponse;
import com.parqueadero.app.models.Audit;
import com.parqueadero.app.models.RoleEntity;
import com.parqueadero.app.models.UserEntity;
import com.parqueadero.app.repositories.UserRepository;

@Service
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;

    private IRoleService roleService;

    public UserServiceImpl(UserRepository userRepository, IRoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {

        List<RoleEntity> roles = new ArrayList<>();

        userRequest.getRoles().forEach(role -> {
            this.roleService.findRoleEntityByName(role.getName()).ifPresent(roles::add);
        });
        
        UserEntity user = UserEntity.builder()
            .email(userRequest.getEmail())
            .password(userRequest.getPassword())
            .roles(roles)
            .audit(new Audit())
            .build();

        user = this.userRepository.save(user);

        return UserResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .isActive(user.getAudit().isActive())
                    .build();
    }

    @Override
    public UserResponse updateUser(Long idUser, UserRequest userRequest) {

        Optional<UserEntity> userEntity = this.userRepository.findById(idUser);

        if(!userEntity.isPresent()) {
            return null;
        }

        //Validar que no exista otro usuario por el correo que viene de la peticion
        
        List<RoleEntity> roles = new ArrayList<>();
        userRequest.getRoles().forEach(role -> {
            this.roleService.findRoleEntityByName(role.getName()).ifPresent(roles::add);
        });
        
        UserEntity user = userEntity.get();
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setRoles(roles);

        user = this.userRepository.save(user);

        return UserResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .isActive(user.getAudit().isActive())
                    .build();
    }
}
