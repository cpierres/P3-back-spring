package com.cpierres.p3backspring.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
