package com.cpierres.p3backspring.controller;

import com.cpierres.p3backspring.entities.User;
import com.cpierres.p3backspring.mappers.UserMapper;
import com.cpierres.p3backspring.model.AuthSuccess;
import com.cpierres.p3backspring.model.LoginRequest;
import com.cpierres.p3backspring.model.RegisterRequest;
import com.cpierres.p3backspring.model.UserDto;
import com.cpierres.p3backspring.services.AuthService;
import com.cpierres.p3backspring.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
    private final UserMapper userMapper;
    //private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthController(
            AuthService authService,
            JwtService jwtService,
            // AuthenticationManager authenticationManager
            UserMapper userMapper
    ) {
        this.authService = authService;
        this.jwtService = jwtService;
        //this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Authentification d'un utilisateur déjà enregistré, via son email",
            description = "L'utilisateur sera connecté via une authentification stateless")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès : retour du token JWT ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthSuccess.class))),
            @ApiResponse(responseCode = "401", description = "Login ou mot de passe incorrect !")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("*** AuthController.login ***");
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
//            );

        //userId non null si utilisateur connu
        Integer userId = authService.login(loginRequest);
        if (userId != null) {

            // Générer un JWT après authentification réussie (avec l'id user qui nous sera utile + tard)
            String token = jwtService.generateToken(userId, loginRequest.getEmail());
            return ResponseEntity.ok(new AuthSuccess(token)); // Retourne le JWT au client
        } else {
            return ResponseEntity.status(401).body("Login ou mot de passe incorrect !");
        }
    }

    @Operation(summary = "Enregistrement d'un utilisateur",
            description = "Suite à son enregistrement, le nouvel utilisateur est directement connecté (authentification stateless Bearer jwt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès : retour du token JWT ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthSuccess.class))),
            @ApiResponse(responseCode = "400", description = "Raison(s) de l'erreur (validation (de RegisterRequest)")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            log.debug("*** AuthController.registerUser ***");
            User user = authService.registerNewUser(request);
            String token = jwtService.generateToken(user.getId(), user.getEmail());
            return ResponseEntity.ok(new AuthSuccess(token));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Affichage de l'utilisateur connecté. Cet api est évidemment protégée.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information sur l'utilisateur connecté (sans le mot de passe)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401", description = "Impossible de retrouver l'utilisateur connecté")
    })
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            UserDto currentUser = userMapper.userToUserDto(authService.getAuthenticatedUser());
            return ResponseEntity.ok(currentUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("Impossible de retrouver l'utilisateur connecté !");
        }
    }
}
