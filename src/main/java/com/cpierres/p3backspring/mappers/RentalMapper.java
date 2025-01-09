package com.cpierres.p3backspring.mappers;

import com.cpierres.p3backspring.entities.Rental;
import com.cpierres.p3backspring.model.RentalSourceDto;
import com.cpierres.p3backspring.model.RentalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface RentalMapper {
    // DIFFICULTE : Expose uniquement l'ID de l'owner (évite pb de sérialisation lors du GET rentals -
    // lié à la relation lazy avec user)
    @Mapping(source = "owner.id", target = "ownerId")
    //@Mapping(target = "picture", ignore = true)
    RentalDto rentalToRentalDto(Rental rental);

    Rental rentalDtoToRental(RentalDto rentalDto);

    // Méthode générique de mapping
    @Mapping(source = "picture", target = "picture")
    RentalDto rentalSourceDtoToRentalDto(RentalSourceDto source);

    // Méthode explicite pour convertir MultipartFile en String
    default String map(MultipartFile value) {
        return value != null ? "https://blog.technavio.org/wp-content/uploads/2018/12/"+value.getOriginalFilename() : null; // Récupère le nom du fichier
    }
}