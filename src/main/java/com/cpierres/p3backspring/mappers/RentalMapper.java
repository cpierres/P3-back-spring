package com.cpierres.p3backspring.mappers;

import com.cpierres.p3backspring.entities.Rental;
import com.cpierres.p3backspring.model.RentalDetailDto;
import com.cpierres.p3backspring.model.RentalMultipartDto;
import com.cpierres.p3backspring.model.RentalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface RentalMapper {
    // DIFFICULTE : Expose uniquement l'ID de l'owner (évite pb de sérialisation lors du GET rentals -
    // lié à la relation lazy avec user)
    @Mapping(source = "owner.id", target = "owner_id")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    RentalDto rentalToRentalDto(Rental rental);

    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    Rental rentalDtoToRental(RentalDto rentalDto);

    // Méthode générique de mapping
    @Mapping(source = "picture", target = "picture")
    RentalDto rentalMultipartDtoToRentalDto(RentalMultipartDto source);

    // Méthode explicite pour convertir MultipartFile en String
    default String map(MultipartFile value) {
        return value != null ? "https://blog.technavio.org/wp-content/uploads/2018/12/"+value.getOriginalFilename() : null; // Récupère le nom du fichier
    }


//    @Mapping(source = "picture", target = "picture")
//    RentalDto rentalDetailDtoToRentalDto(RentalDetailDto rentalDetailDto);

    @Mapping(source = "owner.id", target = "owner_id")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    @Mapping(source = "picture", target = "picture", qualifiedByName = "mapTabFirstEltToString")
    RentalDetailDto rentalToRentalDetailDto(Rental rental);

    // Custom mapping pour convertir String vers String[]
    @Named("mapTabFirstEltToString")
    default String[] mapTabFirstEltToString(String value) {
        return value != null ? new String[]{value} : new String[0];
    }

}