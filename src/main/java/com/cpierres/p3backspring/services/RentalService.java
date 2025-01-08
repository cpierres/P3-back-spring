package com.cpierres.p3backspring.services;

import com.cpierres.p3backspring.entities.Rental;
import com.cpierres.p3backspring.mappers.RentalMapper;
import com.cpierres.p3backspring.model.RentalDto;
import com.cpierres.p3backspring.repositories.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;

    @Autowired
    public RentalService(RentalRepository rentalRepository,
                         RentalMapper rentalMapper) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
    }

    /**
     * Méthode pour créer un Rental
     *
     * @param rentalDto Objet RentalDto à créer
     * @return Rental créé
     */
    public Rental createRental(RentalDto rentalDto) {
        log.debug("*** RentalService.createRental ***");

        // Mapper RentalDto vers Rental
        Rental rental = rentalMapper.rentalDtoToRental(rentalDto);

        // Sauvegarder le Rental dans la base de données
        return rentalRepository.save(rental);
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }
}
