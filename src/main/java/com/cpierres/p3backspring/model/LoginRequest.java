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

    @Schema(description = "L'adresse e-mail doit être unique dans le système", example = "cpi@gmail.com")
    @Email(message = "L'adresse e-mail doit être valide")
    @NotBlank(message = "L'adresse e-mail ne peut pas être vide")
    private String email;

    @Schema(description = "Mot de passe", example = "pw")
    @NotBlank(message = "Le mot de passe ne peut pas être vide.")
//    @jakarta.validation.constraints.Pattern(
//            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,}$",
//            message = "Le mot de passe doit contenir au moins 5 caractères, dont au moins 1 lettre majuscule, 1 lettre minuscule, 1 chiffre et 1 caractère spécial."
//    )
    private String password;
}
