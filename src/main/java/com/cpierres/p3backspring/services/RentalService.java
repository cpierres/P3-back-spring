package com.cpierres.p3backspring.services;

import com.cpierres.p3backspring.entities.Rental;
import com.cpierres.p3backspring.entities.User;
import com.cpierres.p3backspring.mappers.RentalMapper;
import com.cpierres.p3backspring.model.RentalDto;
import com.cpierres.p3backspring.model.RentalSourceDto;
import com.cpierres.p3backspring.model.UserDto;
import com.cpierres.p3backspring.repositories.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final AuthService authService;

    @Autowired
    public RentalService(RentalRepository rentalRepository,
                         RentalMapper rentalMapper,
                         AuthService authService) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
        this.authService = authService;
    }

    /**
     * Méthode pour créer un Rental
     *
     * @param rentalSourceDto Objet RentalDto à créer
     * @return Rental créé
     */
    public Rental createRental(RentalSourceDto rentalSourceDto) {
        log.debug("*** RentalService.createRental ***");

        // Mapper RentalDto vers Rental
        UserDto userDto = this.authService.getAuthenticatedUser();
        RentalDto rentalDto = rentalMapper.rentalSourceDtoToRentalDto(rentalSourceDto);
        //rentalDto.setOwnerId(userDto.getId());//DIFFICULTE
        Rental rental = rentalMapper.rentalDtoToRental(rentalDto);
        rental.setOwner(User.builder().id(userDto.getId()).build());

        // Vérifier si un fichier est présent et l'enregistrer.
        // Simuler une extraction d'URL à partir du fichier.
//        if (rentalDto.getPicture() != null && !rentalDto.getPicture().isEmpty()) {
//            // extraction de l'URL à partir d'un jeton ou d'un service cloud.
//            String pictureUrl = extractUrlFromMultipartFile(rentalDto.getPicture());
//            rental.setPicture(pictureUrl);
//        }


        // Sauvegarder le Rental dans la base de données
        return rentalRepository.save(rental);
    }

    private String extractUrlFromMultipartFile(MultipartFile file) {
        // Simuler l'extraction d'URL.
        // Ici, cette méthode pourrait appeler un service cloud (AWS S3, GCP, etc.).
        return "https://blog.technavio.org/wp-content/uploads/2018/12/" + file.getOriginalFilename();
    }


    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }
}
