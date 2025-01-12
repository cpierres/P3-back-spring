package com.cpierres.p3backspring.controller;

import com.cpierres.p3backspring.model.UserDto;
import com.cpierres.p3backspring.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Récupérer le détail d'un utilisateur via son ID (authentification requise)",
            description = "Récupération des informations détaillées sur un utilisateur grâce à son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Détail de l'utilisateur récupéré avec succès.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable."),
            @ApiResponse(responseCode = "401", description = "Non autorisé. Authentification requise pour accéder à cet endpoint.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(
            @Parameter(description = "Identifiant unique de l'utilisateur", required = true)
            @PathVariable Integer id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
