package com.cpierres.p3backspring.controller;

import com.cpierres.p3backspring.entities.Rental;
import com.cpierres.p3backspring.mappers.RentalMapper;
import com.cpierres.p3backspring.model.RentalDto;
import com.cpierres.p3backspring.model.RentalSourceDto;
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
    public List<RentalDto> getAllRentals() {
        //erreur de sérialisation à cause du lazy loading (Rental -> owner -> User)
        //return rentalService.getAllRentals();

        //passer par DTO pour ne garder que les attributs utiles
        return rentalService.getAllRentals()
                .stream()
                .map(rentalMapper::rentalToRentalDto) // Mapper chaque Rental en DTO
                .toList();
    }
}