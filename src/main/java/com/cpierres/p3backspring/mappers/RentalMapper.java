package com.cpierres.p3backspring.mappers;

import com.cpierres.p3backspring.entities.Rental;
import com.cpierres.p3backspring.model.RentalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RentalMapper {
    // Expose uniquement l'ID de l'owner (évite pb de sérialisation lors du GET rentals -
    // lié à la relation lazy avec user)
    @Mapping(source = "owner.id", target = "ownerId")
    RentalDto rentalToRentalDto(Rental rental);

    Rental rentalDtoToRental(RentalDto rentalDto);
}