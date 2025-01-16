package com.cpierres.p3backspring.controller;

import com.cpierres.p3backspring.entities.Message;
import com.cpierres.p3backspring.model.MessageRequest;
import com.cpierres.p3backspring.model.MessageResponse;
import com.cpierres.p3backspring.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "message-controller",
        description = """
                Cette API permet la gestion des messages. Pour l'instant, seul la création des messages d'un utilisateur
                 pour une location donnée est possible. Ultérieurement, sera développé des endpoints pour lister
                 les messages et les supprimer.
                """
)
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService; // Service métier dédié aux messages

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    @Operation(
            summary = "Créer un message",
            description = "Cette méthode permet de créer un message à partir des données fournies dans le corps de la requête.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Le message a été créé avec succès.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MessageResponse.class))),
                    @ApiResponse(responseCode = "400", description = "La requête est invalide.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MessageResponse.class))),
            }
    )
    public ResponseEntity<MessageResponse> createMessage(@Valid @RequestBody MessageRequest messageRequestDto) {
        Message createdMessage = messageService.createMessage(messageRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(createdMessage.getMessage()));
    }
}