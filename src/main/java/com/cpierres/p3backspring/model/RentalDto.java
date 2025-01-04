package com.cpierres.p3backspring.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class RentalDto {
    private Integer id;
    private String name;
    private Double surface;
    private Double price;
    private String picture;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Integer ownerId;
}
