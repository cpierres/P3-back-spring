package com.cpierres.p3backspring.controller;

import com.cpierres.p3backspring.entities.Rental;
import com.cpierres.p3backspring.mappers.RentalMapper;
import com.cpierres.p3backspring.model.RentalDto;
import com.cpierres.p3backspring.services.RentalService;
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

    /**
     * Endpoint pour créer un nouvel objet Rental
     *
     * @param rental Rental reçu dans le body de la requête via un formData
     * @return Rental sauvegardé
     */
    @PostMapping
    public ResponseEntity<Rental> createRental(@ModelAttribute Rental rental) {
        Rental createdRental = rentalService.createRental(rental);
        return new ResponseEntity<>(createdRental, HttpStatus.CREATED);
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