package com.parqueadero.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parqueadero.app.dtos.requests.UserRequest;
import com.parqueadero.app.dtos.responses.UserResponse;
import com.parqueadero.app.exceptions.BadRequestException;
import com.parqueadero.app.exceptions.NotFoundException;
import com.parqueadero.app.models.Audit;
import com.parqueadero.app.models.RoleEntity;
import com.parqueadero.app.models.UserEntity;
import com.parqueadero.app.repositories.UserRepository;
import com.parqueadero.app.services.interfaces.IRoleService;
import com.parqueadero.app.services.interfaces.IUserService;

@Service
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;

    private IRoleService roleService;
    
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                        IRoleService roleService,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponse> findAllUsers() {

        List<UserEntity> users = (List<UserEntity>) this.userRepository.findAll();

        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach(usr -> {
            userResponses.add(UserResponse.builder().id(usr.getId())
                                .email(usr.getEmail())
                                .isActive(usr.getAudit().isActive())
                                .build());
        });

        return userResponses;
    }

    @Transactional
    @Override
    public UserResponse createUser(UserRequest userRequest) {

        Optional<UserEntity> userRegistered = this.userRepository.findByEmail(userRequest.getEmail());

        if(userRegistered.isPresent()) {
            throw new BadRequestException("email", "the email is already registered");
        }

        List<RoleEntity> roles = new ArrayList<>();

        userRequest.getRoles().forEach(role -> {
            roles.add(this.roleService.findRoleEntityByName(role.getName()).orElseThrow(() -> new NotFoundException("role", "The role with that name was not found"))) ;
        });
        
        UserEntity user = UserEntity.builder()
            .email(userRequest.getEmail())
            .password(passwordEncoder.encode(userRequest.getPassword()))
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

    @Transactional
    @Override
    public UserResponse updateUser(Long idUser, UserRequest userRequest) {

        UserEntity userEntity = this.findUserById(idUser);

        //Validar que no exista otro usuario por el correo que viene de la peticion
        Optional<UserEntity> userRegistered = this.userRepository.findByEmail(userRequest.getEmail());

        if(userRegistered.isPresent()) {
            throw new BadRequestException("email", "the email is already registered");
        }
        
        List<RoleEntity> roles = new ArrayList<>();
        userRequest.getRoles().forEach(role -> {
            this.roleService.findRoleEntityByName(role.getName()).ifPresent(roles::add);
        });
        
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userEntity.setRoles(roles);

        userEntity = this.userRepository.save(userEntity);

        return UserResponse.builder()
                    .id(userEntity.getId())
                    .email(userEntity.getEmail())
                    .isActive(userEntity.getAudit().isActive())
                    .build();
    }

    @Transactional(readOnly = true)
    @Override
    public UserEntity findUserById(Long id) {
        return this.checkOptionalEmpty(this.userRepository.findById(id));
    }

    @Override
    public UserEntity checkOptionalEmpty(Optional<UserEntity> userOptional) {
        if(userOptional.isEmpty()) {
            throw new BadRequestException("Id", "There is no user with that id");
        }
        return userOptional.get();
    }
}
