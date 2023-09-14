package com.raulino.jwtstudyspring.models;

import com.raulino.jwtstudyspring.models.enums.TokenType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @Builder @NoArgsConstructor @AllArgsConstructor 
@Table(name = "token")
public class TokenModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token", nullable = false)
    private String token;
    @Column(name = "token_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    @Column(name = "is_expired", nullable = false)
    private boolean expired;
    @Column(name = "is_revocked", nullable = false)
    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private LocalUser user;

    
}
