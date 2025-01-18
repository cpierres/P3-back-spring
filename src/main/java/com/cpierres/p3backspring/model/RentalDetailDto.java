package com.cpierres.p3backspring.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Schema(description = """
Détails d'une location. Utile pour PUT /rentals/{id} et GET /rentals/{id}
picture est représenté comme un String[]
""")
@Setter
@Getter
@Builder
@AllArgsConstructor
public class RentalDetailDto {
    private Integer id;
    @Schema(description = "Le libellé de la location", example = "Appartement Tourcoing")
    @NotBlank(message = "Le libellé de la location ne peut être vide")
    private String name;

    @Schema(description = "La superficie de la location", example = "110")
    @NotNull(message = "Le superficie doit être renseignée")
    private Integer surface;

    @Schema(description = "Le prix de la location", example = "90")
    @NotNull(message = "Le prix doit être renseigné")
    private Integer price;

    @Schema(description = "url de la photo", examples = "[\"https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg\"]")
    private String[] picture;

    @Schema(description = "Description de la location", maxLength = 2000, example = "Disponible en mars, juin et juillet")
    @NotBlank(message = "La description de la location ne peut être vide")
    @Size(max=2000)
    private String description;

    @Schema(description = "Id de l'utilisateur propriétaire (mis à jour par le système)")
    private Integer owner_id;

    @Schema(description = "Date/heure de création en lecture seule car gérée par le système")
    private Instant created_at;

    @Schema(description = "Date/heure de mise à jour gérée par le système")
    private Instant updated_at;
}
