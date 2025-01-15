package com.cpierres.p3backspring.controller;

import com.cpierres.p3backspring.entities.Rental;
import com.cpierres.p3backspring.mappers.RentalMapper;
import com.cpierres.p3backspring.model.*;
import com.cpierres.p3backspring.services.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    @Autowired
    public RentalController(RentalService rentalService, RentalMapper rentalMapper) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
    }

    @Operation(
            summary = "Créer une location",
            description = "Ajoute une location à partir des données fournies dans la requête, y compris une image pour picture.",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = RentalMultipartDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Location créée avec succès.",
                           content = @Content(mediaType = "application/json", schema = @Schema(implementation = RentalResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Les données de requête sont invalides.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RentalResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RentalResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Erreur lors du téléchargement de l'image.",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = MessageResponse.class)))
            }
    )
    @PostMapping
    public ResponseEntity<RentalResponse> createRental(
            @ModelAttribute RentalMultipartDto rentalMultipartDto) {
        // Appeler le service avec le DTO
        Rental savedRental = rentalService.createRental(rentalMultipartDto);

        // Selon spécification présente dans Mockoon, on ne retourne pas l'entité nouvellement créée
        // mais un RentalResponse avec un simple message
        RentalResponse rentalResponse = new RentalResponse("Location créée !");

        // Retourner la réponse HTTP
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalResponse);
    }

    @GetMapping
    @Operation(
            summary = "Récupérer la liste de toutes les locations",
            description =
                    """
                    Renvoie la liste des locations sous forme d'objets RentalDto. 
                    Cette méthode passe par des DTOs pour éviter les problèmes de sérialisation liés au lazy loading.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des locations récupérée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalsResponse.class)))
    })
    public RentalsResponse getAllRentals() {
        //erreur de sérialisation à cause du lazy loading (Rental -> owner -> User)
        //return rentalService.getAllRentals();

        //passer par DTO pour ne garder que les attributs utiles
        List<RentalDto> list = rentalService.getAllRentals()
                .stream()
                .map(rentalMapper::rentalToRentalDto) // Mapper chaque Rental en DTO
                .toList();

        return new RentalsResponse(list);
    }

    @Operation(
            summary = "Récupérer une location par ID",
            description = "Retourne les détails complets d'une location en fonction de son ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Détails de la location retournés avec succès.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RentalDetailDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Aucune location trouvée pour l'identifiant fourni.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RentalResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public RentalDetailDto getRentalDetail(@PathVariable Integer id) {
        return rentalService.getRentalById(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Mettre à jour une location",
            description = "Cette opération permet de mettre à jour une location existante en fournissant son identifiant unique et les détails à mettre à jour dans le corps de la requête.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Identifiant unique de la location à mettre à jour",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody = @RequestBody(
                    description = "Détails de la location à mettre à jour sous forme d'objet JSON (note : picture dans String[])",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentalDetailDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Mise à jour effectuée avec succès.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RentalResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requête invalide, les données ou paramètres soumis sont incorrects.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RentalResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Aucune location trouvée pour l'identifiant fourni.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RentalResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<RentalResponse> updateRental(
            @Parameter(description = "Identifiant unique de la location", required = true)
            @PathVariable Integer id,
            @RequestBody(description = "Détails de la location fournis dans le corp de la requête", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalDetailDto.class)))
            @ModelAttribute RentalDetailDto rentalDetailDto) {
        RentalDto updatedRental = rentalService.updateRental(id, rentalDetailDto);//peut lancer RentalNotFoundException
        return ResponseEntity.ok(new RentalResponse("Location "+updatedRental.getName()+" mise à jour !"));
    }
}