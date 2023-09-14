package com.raulino.jwtstudyspring.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.raulino.jwtstudyspring.models.LocalUser;
import com.raulino.jwtstudyspring.models.TokenModel;
import com.raulino.jwtstudyspring.models.enums.TokenType;
import com.raulino.jwtstudyspring.repositories.TokenRepository;

import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class TokenService {
    
    private final TokenRepository tokenRepository;


    public void revokeAllUserTokens(LocalUser user) {

        List<TokenModel> userTokens = findAllValidTokenByUser(user.getId());

        userTokens.forEach(token -> {
            token.setRevoked(true);
        });

        tokenRepository.saveAll(userTokens);
    }

    public void expireAllUserTokens(LocalUser user) {

        List<TokenModel> userTokens = findAllValidTokenByUser(user.getId());

        userTokens.forEach(token -> {
            token.setExpired(true);
        });

        tokenRepository.saveAll(userTokens);
    }

    private List<TokenModel> findAllValidTokenByUser(Long userId) {
        return tokenRepository.findAllValidTokensByUserId(userId);
    }

    public void persistToken(String token, LocalUser user) {
        var builtToken =  buildTokenModel(token, user);
        tokenRepository.save(builtToken);
    }


    private TokenModel buildTokenModel(String token, LocalUser user) {
        TokenModel tokenModel = 
            TokenModel.builder()
                .token(token)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user).build();

        return tokenModel;
    }
}
