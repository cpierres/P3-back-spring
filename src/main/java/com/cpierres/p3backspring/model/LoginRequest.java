package com.cpierres.p3backspring.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Objet représentant une requête de connexion utilisateur")
@Getter
@Setter
public class LoginRequest {
    @Email(message = "L'adresse e-mail doit être valide")
    @NotBlank(message = "L'adresse e-mail ne peut pas être vide")
    private String email;

    @NotBlank(message = "Le mot de passe ne peut pas être vide.")
    private String password;
}
