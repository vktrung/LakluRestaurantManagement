package com.laklu.pos.auth;

import com.laklu.pos.valueObjects.UserCredentials;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtGuard jwtGuard;

    public boolean attempt(UserCredentials credentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
        );

        return authentication != null && authentication.isAuthenticated();
    }

    public String login(UserCredentials credentials) {
        if (this.attempt(credentials)) {
            return this.jwtGuard.issueToken(credentials);
        }
        return null;
    }
}
