package com.cpierres.p3backspring.controller;

import com.cpierres.p3backspring.entities.User;
import com.cpierres.p3backspring.mappers.UserMapper;
import com.cpierres.p3backspring.model.*;
import com.cpierres.p3backspring.services.AuthService;
import com.cpierres.p3backspring.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "auth-controller",
        description = """
                Cette API permet de gérer l'authentification, l'enregistrement, et les informations des utilisateurs 
                connectés. Les méthodes utilisent des tokens JWT pour une authentification stateless sécurisée.
                """
)
@Slf4j
@RestController
@RequestMapping("/api/auth")
/**
 * Diagramme UML associé :
 * [Diagramme de séquence d'inscription](file://docs/diagrams/sequence_diagrams/register_sequence.puml)
 * Ce fichier `.puml` décrit les interactions pour le flux d'inscription (register).
 */
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
            @ApiResponse(responseCode = "401", description = "Login ou mot de passe incorrect !",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)))
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
            return ResponseEntity.status(401).body(new MessageResponse("Login ou mot de passe incorrect !"));
        }
    }

    @Operation(summary = "Enregistrement d'un utilisateur (doublon sur email interdit)",
            description = """
                    Suite à son enregistrement, le nouvel utilisateur est directement connecté (authentification stateless Bearer jwt
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès : retour du token JWT ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthSuccess.class))),
            @ApiResponse(responseCode = "400", description = "Raison(s) de l'erreur (validation de RegisterRequest)"),
            @ApiResponse(responseCode = "409", description = "Un utilisateur avec cet email existe déjà",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        log.debug("*** AuthController.registerUser ***");
        User user = authService.registerNewUser(request);
        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return ResponseEntity.ok(new AuthSuccess(token));
    }

    @Operation(summary = "Affichage de l'utilisateur connecté. Cet api est évidemment protégée.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information sur l'utilisateur connecté (sans le mot de passe)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "Impossible de retrouver l'utilisateur connecté",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))),
    })
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        UserDto currentUser = userMapper.userToUserDto(authService.getAuthenticatedUser());
        return ResponseEntity.ok(currentUser);
    }
}
