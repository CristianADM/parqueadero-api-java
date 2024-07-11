package com.parqueadero.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parqueadero.app.dtos.requests.UserRequest;
import com.parqueadero.app.dtos.responses.UserResponse;
import com.parqueadero.app.services.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RequestMapping("/users")
@RestController
public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getMethodName() {
        return "Esto es un simple string";
    }
    
    @PostMapping()
    public ResponseEntity<?> creatUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(userRequest));
    }
    
    @PutMapping
    public ResponseEntity<?> updateUser(Long idUser, @RequestBody UserRequest userRequest) {
        UserResponse userResponse = this.userService.updateUser(idUser, userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userRequest);
    }
}
