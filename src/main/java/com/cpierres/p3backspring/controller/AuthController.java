package com.cpierres.p3backspring.controller;

import com.cpierres.p3backspring.model.LoginRequest;
import com.cpierres.p3backspring.model.RegisterRequest;
import com.cpierres.p3backspring.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = authService.login(loginRequest);

        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful!");//TODO token à la place
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
}
