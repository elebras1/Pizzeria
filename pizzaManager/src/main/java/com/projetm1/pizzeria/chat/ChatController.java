package com.projetm1.pizzeria.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatMessageRepository chatMessageRepository;

    public ChatController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @GetMapping("/{expediteurId}/{receveurId}")
    public List<ChatMessage> getMessages(@PathVariable Long expediteurId, @PathVariable Long receveurId) {
        return chatMessageRepository.findByExpediteurIdAndReceveurIdOrReceveurIdAndExpediteurIdOrderByDateEnvoiAsc(
                expediteurId, receveurId, receveurId, expediteurId
        );
    }
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage handleMessage(@Payload ChatMessage message) {
        System.out.println("Message reçu : " + message.getMessage());
        message.setDateEnvoi(LocalDateTime.now());

        // Sauvegarder le message en base de données
        ChatMessage savedMessage = chatMessageRepository.save(message);
        return savedMessage;
    }

}
