package com.raulino.jwtstudyspring.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raulino.jwtstudyspring.models.TokenModel;

public interface TokenRepository extends JpaRepository<TokenModel, Long> {
    
    Optional<TokenModel> findByToken(String token);

    @Query("""
            select t from TokenModel t inner join User u on t.user.id = u.id
            where u.id = :userId and (t.expired = false or t.revoked = false)
            """)
    List<TokenModel> findAllValidTokensByUser(Long userId);
}
