package com.cpierres.p3backspring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class RentalsResponse {
    private List<RentalDto> rentals;
}
