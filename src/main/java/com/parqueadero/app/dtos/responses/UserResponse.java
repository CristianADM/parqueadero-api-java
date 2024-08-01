package com.parqueadero.app.dtos.responses;

import com.parqueadero.app.models.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class UserResponse {

    private Long id;
    private String email;
    private boolean isActive;

    public UserResponse(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
        this.isActive = userEntity.getAudit().isActive();
    }
}
