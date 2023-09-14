package com.raulino.jwtstudyspring.models;

import java.util.List;

import com.raulino.jwtstudyspring.controllers.auth.RegisterRequest;
import com.raulino.jwtstudyspring.models.enums.LocalRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @Builder @AllArgsConstructor @NoArgsConstructor
@Table(name = "local_user")
public class LocalUser {

    public LocalUser(RegisterRequest data) {
        this.firstName = data.getFirstName();
        this.lastName = data.getLastName();
        this.username = data.getUsername();
        this.email = data.getEmail();
        this.password = data.getPassword();
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private LocalRole role;
    @OneToMany(mappedBy = "user")
    private List<TokenModel> tokens;
}
