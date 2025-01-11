package com.cpierres.p3backspring.controller;

import com.cpierres.p3backspring.entities.Rental;
import com.cpierres.p3backspring.mappers.RentalMapper;
import com.cpierres.p3backspring.model.*;
import com.cpierres.p3backspring.services.JwtService;
import com.cpierres.p3backspring.services.RentalService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtService jwtService;

    @Autowired
    public RentalController(RentalService rentalService, RentalMapper rentalMapper, JwtService jwtService) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
        this.jwtService = jwtService;
    }

    /**
     * Endpoint pour créer un nouvel objet Rental
     *
     * @param rentalSourceDto Rental reçu dans le body de la requête via un formData
     * @return Rental sauvegardé
     */
    @PostMapping
    public ResponseEntity<Rental> createRental(@ModelAttribute RentalSourceDto rentalSourceDto, HttpServletRequest request) {
        // Récupérer l'ID utilisateur via JwtService
        Integer ownerId = jwtService.extractUserIdFromRequest(request);

        if (ownerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Associer l'ID utilisateur au DTO
        rentalSourceDto.setOwnerId(ownerId);

        // Appeler le service avec le DTO
        Rental savedRental = rentalService.createRental(rentalSourceDto);

        // Retourner la réponse HTTP
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRental);

    }

    @GetMapping
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

    /**
     * Récupérer le détail d'un Rental par son ID
     *
     * @param id Identifiant du Rental
     * @return RentalDetailDto contenant les informations du Rental
     */
    @GetMapping("/{id}")
    public RentalDetailDto getRentalDetail(@PathVariable Integer id) {
        return rentalService.getRentalById(id);
    }

    /**
     * Mettre à jour une location avec le paramètre id.
     * Données soumises en form data.
     *
     * @param id identifiant unique de la location
     * @param rentalDetailDto données de détails de la location fournies dans le body de la request (note : picture dans tableau).
     * @return A ResponseEntity containing the updated RentalDto object.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RentalDto> updateRental(
            @PathVariable Integer id,
            @ModelAttribute RentalDetailDto rentalDetailDto) {
        RentalDto updatedRental = rentalService.updateRental(id, rentalDetailDto);
        return ResponseEntity.ok(updatedRental);
    }
}