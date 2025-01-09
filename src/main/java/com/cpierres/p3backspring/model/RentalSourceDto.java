package com.cpierres.p3backspring.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Data
@Builder
public class RentalSourceDto {
    private Integer id;
    private String name;
    private Double surface;
    private Double price;
    private MultipartFile picture;
    private String description;
    private Integer ownerId;
    private Instant createdAt;
    private Instant updatedAt;
}
