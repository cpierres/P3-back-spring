package com.cpierres.p3backspring.services;

import com.cpierres.p3backspring.entities.Message;
import com.cpierres.p3backspring.entities.Rental;
import com.cpierres.p3backspring.entities.User;
import com.cpierres.p3backspring.model.MessageRequest;
import com.cpierres.p3backspring.repositories.MessageRepository;
import com.cpierres.p3backspring.repositories.RentalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final RentalRepository rentalRepository;
    private final AuthService authService;

    public MessageService(
            MessageRepository messageRepository,
            RentalRepository rentalRepository,
            AuthService authService) {
        this.messageRepository = messageRepository;
        this.rentalRepository = rentalRepository;
        this.authService = authService;
    }

    @Transactional
    public Message createMessage(MessageRequest messageRequest) {
        log.debug("*** MessageService.createMessage *** "+messageRequest);
        // Récupérer l'utilisateur courant
        User currentUser = authService.getAuthenticatedUser();

        // Récupérer la location liée
        Rental rental = rentalRepository.findById(messageRequest.getRental_id())
                .orElseThrow(() -> new EntityNotFoundException("Location non trouvée"));

        // Créer un nouveau message
        Message message = new Message();
        message.setMessage(messageRequest.getMessage());
        message.setUser(currentUser); // Associer l'utilisateur
        message.setRental(rental);   // Associer la location

        // Sauvegarder le message dans la base de données
        return messageRepository.save(message);
    }
}