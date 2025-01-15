package com.cpierres.p3backspring.mappers;

import com.cpierres.p3backspring.entities.Rental;
import com.cpierres.p3backspring.model.RentalDetailDto;
import com.cpierres.p3backspring.model.RentalDto;
import com.cpierres.p3backspring.model.RentalMultipartDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RentalMapper {

    //Expose uniquement l'ID de l'owner (évite pb de sérialisation lors du GET rentals -
    // lié à la relation lazy avec user => on a besoin uniquement de l'id)
    @Mapping(source = "owner.id", target = "owner_id")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    RentalDto rentalToRentalDto(Rental rental);

    Rental rentalDtoToRental(RentalDto rentalDto);

    @Mapping(target = "picture", source = "picture", ignore = true)//l'upload sera fait dans le service
    RentalDto rentalMultipartDtoToRentalDto(RentalMultipartDto source);

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