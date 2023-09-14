package com.raulino.jwtstudyspring.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.raulino.jwtstudyspring.controllers.auth.AuthenticationRequest;
import com.raulino.jwtstudyspring.controllers.auth.AuthenticationResponse;
import com.raulino.jwtstudyspring.controllers.auth.RegisterRequest;
import com.raulino.jwtstudyspring.models.LocalUser;
import com.raulino.jwtstudyspring.models.TokenModel;
import com.raulino.jwtstudyspring.models.enums.LocalRole;
import com.raulino.jwtstudyspring.models.enums.TokenType;
import com.raulino.jwtstudyspring.repositories.LocalUserRepository;
import com.raulino.jwtstudyspring.repositories.TokenRepository;
import com.raulino.jwtstudyspring.security.LocalUserDetails;

import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class AuthenticationService {

    private final LocalUserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    
    public AuthenticationResponse register(RegisterRequest request) {
        LocalUser newUser = 
            LocalUser.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(LocalRole.USER)
            .build();

        var savedUser = userRepository.save(newUser);
    
        String token = generateAndPersistJwtToken(savedUser);

        var response = AuthenticationResponse.builder().token(token).build();

        return response;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        LocalUser user = userRepository.findByUsername(request.getUsername()).orElseThrow(); 

        String token = generateAndPersistJwtToken(user);

        var response = AuthenticationResponse.builder().token(token).build();

        return response;
    }

    private String generateAndPersistJwtToken(LocalUser user) {
        String token = jwtService.generateToken(new LocalUserDetails(user));
        TokenModel tokenModel = 
            TokenModel.builder()
                .token(token)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user).build();

        tokenRepository.save(tokenModel);

        return token;
    }

}
