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

    @Schema(description = "Le prix de la location", example = "150000")
    @NotNull(message = "Le prix doit être renseigné")
    private Integer price;

    private String[] picture;

    @Schema(description = "Description de la location", maxLength = 2000)
    @NotBlank(message = "Le titre de la location ne peut être vide")
    @Size(max=2000)
    private String description;

    private Integer owner_id;
    private Instant created_at;
    private Instant updated_at;
}
