package com.laklu.pos.auth.config;

import com.laklu.pos.auth.RestAccessDeniedHandler;
import com.laklu.pos.auth.RestAuthenticationEntryPoint;
import com.laklu.pos.filters.JwtFilter;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HttpSession httpSession) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests((authorizeRequests) ->
            authorizeRequests
                .requestMatchers("/api/v1/auth/login").permitAll()
                .anyRequest().authenticated()
        );
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling((exceptionHandling) ->
            exceptionHandling
                .accessDeniedHandler(new RestAccessDeniedHandler())
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
        );
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configurationSource = new CorsConfiguration();
        configurationSource.addAllowedOriginPattern("*");
        configurationSource.addAllowedHeader("*");
        configurationSource.addAllowedMethod(HttpMethod.GET);
        configurationSource.addAllowedMethod(HttpMethod.POST);
        configurationSource.addAllowedMethod(HttpMethod.PUT);
        configurationSource.addAllowedMethod(HttpMethod.DELETE);
        configurationSource.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configurationSource);
        return source;
    }

}
