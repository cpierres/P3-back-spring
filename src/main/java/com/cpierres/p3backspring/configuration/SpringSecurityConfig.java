package com.cpierres.p3backspring.configuration;

//import com.cpierres.p3backspring.services.CustomUserDetailsService;
import com.cpierres.p3backspring.services.JwtService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;
import java.util.Optional;

@Slf4j
@Configuration
//@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SpringSecurityConfig {

//    private final CustomUserDetailsService customUserDetailsService;
//
//    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService) {
//        this.customUserDetailsService = customUserDetailsService;
//    }

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
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll() // Autorisé sans authentification
                        .anyRequest().authenticated() // Protège toutes les autres routes
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
                //.httpBasic(Customizer.withDefaults());
        // Configurer JWT OAuth2 Resource Server
                //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Ajoute le filtre JWT

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

//    @Bean
//    public AuditorAware<Integer> auditorProvider() {
//        log.debug("*** auditorProvider ***");
//        return () -> {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
//                return Optional.empty(); // Aucun utilisateur connecté
//            }
//
//            // Extraire l'identifiant utilisateur depuis CustomUserDetails
//            Object principal = authentication.getPrincipal();
//            if (principal instanceof CustomUserDetails userDetails) {
//                return Optional.ofNullable(userDetails.getId());
//            }
//
//            log.warn("Impossible de récupérer l'identifiant utilisateur. Principal inattendu : {}", principal);
//            return Optional.empty(); // Principal inconnu ou non conforme
//        };
//    }

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(this.customUserDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .authenticationProvider(authenticationProvider())
//                .build();
//    }

}
