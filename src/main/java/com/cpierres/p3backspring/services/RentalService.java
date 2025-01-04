package com.cpierres.p3backspring.services;

import com.cpierres.p3backspring.entities.Rental;
import com.cpierres.p3backspring.repositories.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {
    private final RentalRepository rentalRepository;

    @Autowired
    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    /**
     * Méthode pour créer un Rental
     * @param rental Objet Rental à créer
     * @return Rental créé
     */
    public Rental createRental(Rental rental) {
        System.out.println("RentalService.createRental");
        // Sauvegarder le Rental dans la base de données
        return rentalRepository.save(rental);
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }
}
