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

    /**
     * Configure la chaîne de filtrage de sécurité pour l'application, en définissant des politiques de sécurité telles
     * que la désactivation de CSRF, la gestion des sessions sans état, l'autorisation d'accès à des points de
     * terminaison spécifiques sans authentification et l'exigence d'authentification pour tous les autres itinéraires.
     * Il configure également le serveur de ressources OAuth2 pour utiliser l'authentification JWT.
     *
     * @param http l'objet {@link HttpSecurity} utilisé pour configurer les paramètres de sécurité
     * @return l'instance {@link SecurityFilterChain} construite après l'application de toutes les configurations
     * @throws Exception si une erreur se produit lors de la configuration de la chaîne de filtrage de sécurité
     */
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

    /**
     * Crée et renvoie un bean pour l'interface {@link PasswordEncoder} pour encoder les mots de passe de manière
     * sécurisée.
     * On fournit à Spring Security une implémentation concrète correspondant à l'interface qu'il doit utiliser
     * dans son fonctionnement.
     * On doit configurer ce bean pour Spring parce qu'on est dans le cadre d'un serveur de ressources OAuth2 autonome
     * et qu'on a choisi de gérer nous-même l'authentification et la gestion du token.
     * L'encodeur renvoyé utilise l'algorithme de hachage BCrypt.
     * Ce bean est injectable n'importe où.
     *
     * @return une instance de {@link PasswordEncoder} utilisant l'algorithme de hachage BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("*** passwordEncoder ***");
        return new BCryptPasswordEncoder();
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
