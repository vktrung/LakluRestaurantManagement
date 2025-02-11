package com.laklu.pos.auth;

import com.laklu.pos.valueObjects.UserCredentials;
import com.laklu.pos.valueObjects.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtGuard {

    @Value("${jwt.secret}")
    private String secretKey;

    // as minutes
    @Value("${jwt.expirationTime}")
    private Long expirationTime;

    private final AuthenticationManager authenticationManager;

    public JwtGuard(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public boolean attempt(UserCredentials credentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
        );
        // TODO : throw exception
        return authentication.isAuthenticated();
    }

    public String login(UserCredentials credentials) {
        if (this.attempt(credentials)) {
            return this.issueToken(credentials);
        }
        return null;
    }

    private String issueToken(UserCredentials credentials) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(credentials.getUsername())
                .issuedAt(new Date())
                // TODO: add expiration time to config
                .expiration(new Date(System.currentTimeMillis() * 60 * 60 * this.expirationTime))
                .and()
                .signWith(this.getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractSubject(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return this.extractClaim(token, Claims::getExpiration).before(new Date());
    }


    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean validateToken(String token, UserPrincipal userPrincipal) {
        return userPrincipal.getUsername().equals(this.extractSubject(token)) && !this.isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static UserPrincipal userPrincipal() {
        return (UserPrincipal) getAuthentication().getPrincipal();
    }
}
