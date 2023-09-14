package com.raulino.jwtstudyspring.services;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.raulino.jwtstudyspring.models.TokenModel;
import com.raulino.jwtstudyspring.repositories.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class JwtService {

    private final TokenRepository tokenRepository;

    private static final String SECRET_KEY = "ZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZGRkZA==";

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extractedClaims, UserDetails userDetails) {
        return Jwts
            .builder()
            .setClaims(extractedClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }
    

    public Boolean isTokenValid(String token, UserDetails userDetails) {
    
        Optional<TokenModel> opToken = tokenRepository.findByToken(token);

        if (opToken.isPresent()) {
            var realToken = opToken.get();
            if (!realToken.isExpired() || !realToken.isRevoked()) {
                String username = extractUsernameFromJwt(token);
                return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
            }
        }

        return false;
    }
    
    public String extractUsernameFromJwt(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
