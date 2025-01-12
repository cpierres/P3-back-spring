package com.cpierres.p3backspring.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Schema(description = "Structure représentant les informations détaillées d'un utilisateur.")
@Getter
@Setter
@RequiredArgsConstructor
public class UserDto {

    @Schema(description = "Identifiant unique de l'utilisateur.", example = "1")
    private Integer id;

    @Schema(description = "Nom complet de l'utilisateur.",
            example = "Christophe Pierrès")
    private String name;

    @Schema(description = "Adresse email associée à l'utilisateur. Doit être unique",
            example = "jean.dupont@example.com")
    @Email(message = "L'adresse e-mail doit être valide")
    @NotBlank(message = "L'adresse e-mail ne peut pas être vide")
    private String email;

    private Instant created_at;
    private Instant updated_at;
}
