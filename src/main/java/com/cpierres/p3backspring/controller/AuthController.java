package com.cpierres.p3backspring.controller;

import com.cpierres.p3backspring.model.AuthSuccess;
import com.cpierres.p3backspring.model.LoginRequest;
import com.cpierres.p3backspring.model.RegisterRequest;
import com.cpierres.p3backspring.model.UserDto;
import com.cpierres.p3backspring.services.AuthService;
import com.cpierres.p3backspring.services.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    //private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthController(
            AuthService authService,
            JwtService jwtService
            //, AuthenticationManager authenticationManager
            ) {
        this.authService = authService;
        this.jwtService = jwtService;
        //this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.debug("*** AuthController.login ***");
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
//            );

        //userId non null si utilisateur connu
        Integer userId = authService.login(loginRequest);
        if (userId!=null) {

            // Générer un JWT après authentification réussie (avec l'id user qui nous sera utile + tard)
            String token = jwtService.generateToken(userId, loginRequest.getEmail());
            return ResponseEntity.ok(new AuthSuccess(token)); // Retourne le JWT au client
        } else {
            return ResponseEntity.status(401).body("Invalid login credentials!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        try {
            authService.registerNewUser(request);
            return ResponseEntity.ok("Utilisateur enregistré avec succès");//TODO token à la place
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            UserDto currentUser = authService.getAuthenticatedUser();
            return ResponseEntity.ok(currentUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("Unable to retrieve the authenticated user!");
        }
    }
}
