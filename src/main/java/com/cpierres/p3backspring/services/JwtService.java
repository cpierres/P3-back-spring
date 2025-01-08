package com.cpierres.p3backspring.services;

import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtService {
    private static final long EXPIRATION_TIME = 3600 * 1000; // 1 heure (en millisecondes)
    //private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);//Clé secrète
    private static final String BASE64_SECRET = "G2urFhZkMI9/4z0jAlb5FrYPm8ZtRQjg/XwUN5893LI=";
    private static final SecretKey SECRET_KEY = new SecretKeySpec(
            Base64.getDecoder().decode(BASE64_SECRET), // Décodage Base64
            SignatureAlgorithm.HS256.getJcaName() // Algorithme HMAC-SHA256
    );



    // Générer un token JWT
    public String generateToken(Integer id, String username) {
        String token = Jwts.builder()
                .setSubject(username) // Nom d'utilisateur (claim "sub")
                .claim("id", id)      // Claim personnalisé pour inclure l'ID du user nécessaire
                .setIssuedAt(new Date()) // Date d'émission
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiration
                .signWith(SECRET_KEY) // Signature avec clé secrète
                .compact();
        //log.debug("**** generateToken(id:"+id+", username: "+username+" ) -> "+token+" ****");

        return token;
    }

    // Renvoyer la clé pour la configuration côté Resource Server
    public SecretKey getSecretKey() {
        return SECRET_KEY;
    }

    /**
     * Récupérer l'ID depuis le jeton JWT
     *
     * @param token
     * @return
     */
    public Integer extractIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Clé secrète utilisée pour signer le token
                .build()
                .parseClaimsJws(token) // Analyse et vérification du token
                .getBody(); // Récupère le corps des claims

        return claims.get("id", Integer.class); // Récupère le claim "id" (id user) au format Integer
    }

    /**
     * Récupérer le token JWT depuis l'en-tête Authorization de la requête.
     *
     * @param request La requête HTTP courante.
     * @return Le jeton JWT ou null s'il est absent.
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        // Récupérer l'en-tête Authorization
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extraire le token après "Bearer "
            return authorizationHeader.substring(7);
        }
        return null; // Pas de jeton trouvé
    }

    /**
     * Récupérer l'ID utilisateur du token courant (s'il existe) depuis la requête.
     *
     * @param request La requête HTTP courante.
     * @return L'ID utilisateur extrait ou null si aucun token valide.
     */
    public Integer extractUserIdFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            return extractIdFromToken(token); // Utiliser extractIdFromToken pour récupérer l'ID
        }
        return null; // Pas de token ou token invalide
    }
}
