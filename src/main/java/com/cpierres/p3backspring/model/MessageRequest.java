package com.cpierres.p3backspring.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Objet pour représenter l'envoi d'un message d'un utilisateur pour une location")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MessageRequest {
    @NotNull(message = "rental_id obligatoire")
    private Integer rental_id;

    @NotNull(message = "user_id obligatoire")
    private Integer user_id;

    @NotBlank(message = "Message obligatoire")
    private String message;
}