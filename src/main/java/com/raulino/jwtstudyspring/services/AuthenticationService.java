package com.raulino.jwtstudyspring.services;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raulino.jwtstudyspring.controllers.auth.AuthenticationRequest;
import com.raulino.jwtstudyspring.controllers.auth.AuthenticationResponse;
import com.raulino.jwtstudyspring.controllers.auth.RegisterRequest;
import com.raulino.jwtstudyspring.models.LocalUser;
import com.raulino.jwtstudyspring.models.enums.LocalRole;
import com.raulino.jwtstudyspring.repositories.LocalUserRepository;
import com.raulino.jwtstudyspring.security.LocalUserDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class AuthenticationService {

    private final LocalUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    
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
    
        String token = jwtService.generateEmptyAccessToken(new LocalUserDetails(savedUser));
        String refreshToken = jwtService.generateRefreshToken(new LocalUserDetails(savedUser));
        tokenService.saveToken(token, savedUser);

        var response = AuthenticationResponse.builder().accessToken(token).refreshToken(refreshToken).build();

        return response;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        
        LocalUser user = userRepository.findByUsername(request.getUsername()).orElseThrow(); 


        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );


        tokenService.revokeAllUserTokens(user);
        tokenService.expireAllUserTokens(user);
        String token = jwtService.generateEmptyAccessToken(new LocalUserDetails(user));
        String refreshToken = jwtService.generateRefreshToken(new LocalUserDetails(user));
        tokenService.saveToken(token, user);

        var response = AuthenticationResponse.builder().accessToken(token).refreshToken(refreshToken).build();

        return response;
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsernameFromJwt(refreshToken);

        if (username != null) {
            LocalUser user = userRepository.findByUsername(username).orElseThrow();

            // boolean isRefreshTokenValid = tokenService.isTokenValid(refreshToken);
            var userDetails = new LocalUserDetails(user);
            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                tokenService.revokeAllUserTokens(user);
                tokenService.expireAllUserTokens(user);
                String accessToken = jwtService.generateEmptyAccessToken(userDetails);
                tokenService.saveToken(refreshToken, user);
                var authResponse = 
                    AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}
