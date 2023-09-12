package com.raulino.jwtstudyspring.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raulino.jwtstudyspring.models.LocalUser;

public interface LocalUserRepository extends JpaRepository<LocalUser, Long>{
    
    Optional<LocalUser> findByEmail(String email);

    Optional<LocalUser> findByUsername(String username);
}
