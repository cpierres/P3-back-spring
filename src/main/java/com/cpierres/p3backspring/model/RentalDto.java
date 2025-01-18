package com.cpierres.p3backspring.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Schema(description = """
Détails d'une location. Utile pour GET /rentals (obtenir la liste des locations)
picture est représenté comme un simple String (contrairement à RentalDetailDto et RentalMultipartDto)
""")
@Setter
@Getter
@NoArgsConstructor
public class RentalDto {
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

    @Schema(
            description = "Url de l'image ou photo associée à la location",
            example = "https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg"
    )
    private String picture;

    private String description;

    @Schema(description = "Id de l'utilisateur propriétaire (mis à jour par le système)")
    private Integer owner_id;

    @Schema(description = "Date/heure de création en lecture seule car gérée par le système")
    private Instant created_at;

    @Schema(description = "Date/heure de mise à jour gérée par le système")
    private Instant updated_at;
}
