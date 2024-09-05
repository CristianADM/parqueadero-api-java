package com.parqueadero.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

        Optional<UserEntity> userRegistered = this.userRepository.findByEmailAndAuditIsActiveIsTrue(userRequest.getEmail());

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
        Optional<UserEntity> userRegistered = this.userRepository.findByEmailAndAuditIsActiveIsTrue(userRequest.getEmail());

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
    
    @Transactional(readOnly = true)
    @Override
    public UserEntity findUserByEmail(String email) {
        return this.checkOptionalEmpty(this.userRepository.findByEmailAndAuditIsActiveIsTrue(email));
    }

    @Override
    public UserEntity checkOptionalEmpty(Optional<UserEntity> userOptional) {
        if(userOptional.isEmpty()) {
            throw new BadRequestException("Id", "There is no user with that id");
        }
        return userOptional.get();
    }

    // @Transactional(readOnly = true)
    // @Override
    // public UserEntity getLoggedInUsername() {
    //     // Obtener el objeto Authentication
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    //     String email = "";
        
    //     // Verificar que la autenticación no sea nula y esté autenticada
    //     if (authentication != null && authentication.isAuthenticated()) {

    //         // Obtener el principal del objeto Authentication
    //         Object principal = authentication.getPrincipal();
            
    //         // Verificar si el principal es una instancia de UserDetails
    //         if (principal instanceof UserDetails) {
    //             // Retornar el nombre de usuario
    //             email = ((UserDetails) principal).getUsername();
    //         } else {
    //             // Si el principal no es un UserDetails, puede ser un String (por ejemplo, el nombre de usuario)
    //             email = principal.toString();
    //         }

    //         return this.findUserByEmail(email);
    //     }
    //     return null; // Si no está autenticado o no se pudo obtener el nombre de usuario
    // }
    
    @Override
    public boolean isAdmin(UserEntity user) {
        return user.getRoles().stream()
            .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));
    }
}