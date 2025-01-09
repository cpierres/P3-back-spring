package com.cpierres.p3backspring.model;

import lombok.Data;

import java.time.Instant;

@Data
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private Instant created_at;
    private Instant updated_at;
}
