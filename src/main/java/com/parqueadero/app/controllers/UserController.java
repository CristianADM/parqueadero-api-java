package com.parqueadero.app.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parqueadero.app.dtos.requests.UserRequest;
import com.parqueadero.app.dtos.responses.UserResponse;
import com.parqueadero.app.services.interfaces.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RequiredArgsConstructor

@RequestMapping("/users")
@RestController
public class UserController {

    private final IUserService userService;

    //---- GET ----\\
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findAllUsers()); 
    }
    
    //---- POST ----\\
    @PostMapping()
    public ResponseEntity<UserResponse> creatUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(userRequest));
    }
    
    //---- PUT ----\\
    @PutMapping("/{idUser}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long idUser, @RequestBody UserRequest userRequest) {
        UserResponse userResponse = this.userService.updateUser(idUser, userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }
}
