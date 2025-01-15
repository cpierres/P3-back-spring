package com.cpierres.p3backspring.services;

import com.cpierres.p3backspring.entities.Rental;
import com.cpierres.p3backspring.entities.User;
import com.cpierres.p3backspring.exception.ResourceNotFoundException;
import com.cpierres.p3backspring.mappers.RentalMapper;
import com.cpierres.p3backspring.mappers.UserMapper;
import com.cpierres.p3backspring.model.RentalDetailDto;
import com.cpierres.p3backspring.model.RentalDto;
import com.cpierres.p3backspring.model.RentalMultipartDto;
import com.cpierres.p3backspring.model.UserDto;
import com.cpierres.p3backspring.repositories.RentalRepository;
import com.cpierres.p3backspring.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FileUploadService fileUploadService;

    @Autowired
    public RentalService(RentalRepository rentalRepository,
                         RentalMapper rentalMapper,
                         AuthService authService,
                         UserRepository userRepository,
                         UserMapper userMapper,
                         FileUploadService fileUploadService) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
        this.authService = authService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.fileUploadService = fileUploadService;
    }

    /**
     * Méthode pour créer un Rental
     *
     * @param rentalMultipartDto Objet RentalDto à créer
     * @return Rental créé
     */
    public Rental createRental(RentalMultipartDto rentalMultipartDto) {
        log.debug("*** RentalService.createRental ***");

        // Télécharger l'image reçue dans le DTO avec Cloudinary
        String pictureUrl = fileUploadService.uploadFile(rentalMultipartDto.getPicture());

        // Mapper RentalDto vers Rental
        RentalDto rentalDto = rentalMapper.rentalMultipartDtoToRentalDto(rentalMultipartDto);
        rentalDto.setPicture(pictureUrl);

        Rental rental = rentalMapper.rentalDtoToRental(rentalDto);
        UserDto userDto = userMapper.userToUserDto(this.authService.getAuthenticatedUser());
        rental.setOwner(User.builder().id(userDto.getId()).build());

        // Sauvegarder le Rental dans la base de données
        return rentalRepository.save(rental);
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * Récupérer le détail d'un Rental par son ID
     *
     * @param id Identifiant du Rental
     * @return RentalDetailDto contenant les informations du Rental
     */
    public RentalDetailDto getRentalById(Integer id) {
        log.debug("Fetching rental detail for ID: {}", id);

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location id: " + id + " non trouvée"));

        // Mapper l'entité Rental vers RentalDetailDto
        return rentalMapper.rentalToRentalDetailDto(rental);
    }


    public RentalDto updateRental(Integer id, RentalDetailDto rentalDetailDto) {
        // Vérifie si la location existe
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location avec id: " + id + " non trouvé"));

        // Mettre à jour les propriétés de l'entité existante avec les valeurs reçues
        rental.setName(rentalDetailDto.getName());
        rental.setSurface(rentalDetailDto.getSurface());
        rental.setPrice(rentalDetailDto.getPrice());
        String[] pictures = rentalDetailDto.getPicture();
        if (pictures != null && pictures.length > 0) {
            rental.setPicture(pictures[0]);
        }

        rental.setDescription(rentalDetailDto.getDescription());
        if (rentalDetailDto.getOwner_id() != null) {
            User existingUser = userRepository.findById(rentalDetailDto.getOwner_id())
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
            rental.setOwner(existingUser);
        }

        // Sauvegarder l'entité mise à jour
        Rental updatedRental = rentalRepository.save(rental);

        // Retourner un DTO mis à jour
        return rentalMapper.rentalToRentalDto(updatedRental);
    }
}
