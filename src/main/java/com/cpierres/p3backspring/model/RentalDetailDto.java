package com.cpierres.p3backspring.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class RentalDetailDto {
    private Integer id;
    private String name;
    private BigDecimal surface;
    private BigDecimal price;
    private String[] picture;
    private String description;
    private Integer owner_id;
    private Instant created_at;
    private Instant updated_at;
}
