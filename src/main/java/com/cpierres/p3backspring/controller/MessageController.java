package com.cpierres.p3backspring.controller;

import com.cpierres.p3backspring.entities.Message;
import com.cpierres.p3backspring.model.MessageRequest;
import com.cpierres.p3backspring.model.MessageResponse;
import com.cpierres.p3backspring.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService; // Service métier dédié aux messages

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    //public ResponseEntity<Message> createMessage(@RequestBody MessageRequest messageRequestDto) {
    public ResponseEntity<MessageResponse> createMessage(@RequestBody MessageRequest messageRequestDto) {
        Message createdMessage = messageService.createMessage(messageRequestDto);
        //return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(createdMessage.getMessage()));
    }
}