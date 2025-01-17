package com.cpierres.p3backspring.configuration;

//import com.cpierres.p3backspring.services.CustomUserDetailsService;

import com.cpierres.p3backspring.services.JwtService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
public class SpringSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("*** passwordEncoder ***");
         return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("*** securityFilterChain ***");
         http
                .csrf(csrf -> csrf.disable())//inutile pour rest (utile si cookie)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Pas de sessions côté serveur
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll() // Autorisé sans authentification
                        .anyRequest().authenticated() // Protège toutes les autres routes
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    // Configurer le décodeur JWT et utiliser la clé secrète
    @Bean
    public JwtDecoder jwtDecoder(JwtService jwtService) {
        log.debug("*** jwtDecoder ***");
        return NimbusJwtDecoder.withSecretKey(jwtService.getSecretKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(JwtService jwtService) {
        log.debug("*** jwtEncoder ***");
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtService.getSecretKey()));
    }

}
