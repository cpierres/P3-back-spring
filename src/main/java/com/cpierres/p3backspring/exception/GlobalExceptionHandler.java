package com.cpierres.p3backspring.exception;

import com.cpierres.p3backspring.model.MessageResponse;
import com.cpierres.p3backspring.model.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Permet d'afficher un message synthétique lors de la validation des DTO
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ValidationErrorResponse response = new ValidationErrorResponse(
                "Les données d'entrée ne sont pas valides.",
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Gestion de UploadFailedException avec une réponse structurée comme un MessageResponse,
     * structure habituelle de cette application
     *
     * @param ex exception du serveur lors du téléchargement
     * @return ResponseEntity contenant un MessageResponse avec le statut HTTP 500 (INTERNAL_SERVER_ERROR)
     * et un message décrivant l'erreur spécifique liée au téléchargement de l'image.
     */
    @ExceptionHandler(UploadFailedException.class)
    public ResponseEntity<MessageResponse> handleUploadFailedException(UploadFailedException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // Code 500
                .body(new MessageResponse("Erreur lors du téléchargement de l'image : " + ex.getMessage()));
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<MessageResponse> handleResourceAlreadyExistException(ResourceAlreadyExistException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // Code 409
                .body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // Code 404
                .body(new MessageResponse(ex.getMessage()));
    }
}

