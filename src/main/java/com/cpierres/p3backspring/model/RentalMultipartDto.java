package com.cpierres.p3backspring.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@Schema(description =
        """ 
        Objet représentant la création d'une location avec upload d'une image (en MultipartFile).
        Cette image doit exister sur un serveur HTTP. QUESTION MENTOR
        """)
public class RentalMultipartDto {
    @Schema(description = "Le libellé de la location", example = "Appartement Tourcoing")
    @NotBlank(message = "Le libellé de la location ne peut être vide")
    private String name;

    @Schema(description = "La superficie de la location", example = "110")
    @NotNull(message = "Le superficie doit être renseignée")
    private Integer surface;

    @Schema(description = "Le prix de la location", example = "150000")
    @NotNull(message = "Le prix doit être renseigné")
    private Integer price;

    @Schema(
            description = "Image ou photo associée à la location",
            type = "string", // Swagger lit `MultipartFile` en tant que fichier string
            format = "binary",
            example= "https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg"
    )
    @NotNull(message = "Une photo doit être choisie ; QUESTION")
    private MultipartFile picture;

    @Schema(description = "Description de la location", maxLength = 2000)
    @NotBlank(message = "Le titre de la location ne peut être vide")
    @Size(max=2000)
    private String description;

//    private Integer ownerId;

//    private Instant createdAt;
//    private Instant updatedAt;
}
