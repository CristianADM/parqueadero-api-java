package com.parqueadero.app.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Embeddable
public class Audit {

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_At")
    private LocalDateTime createAt;

    @Column(name = "updated_At")
    private LocalDateTime updateAt;

    @PrePersist
    public void prePersist() {
        this.isActive = true;
        this.createAt = LocalDateTime.now().withNano(0).withSecond(0);
        this.updateAt = this.createAt;
    }

    @PreUpdate
    public void PreUpdate() {
        this.updateAt = LocalDateTime.now().withNano(0).withSecond(0);
    }
}
