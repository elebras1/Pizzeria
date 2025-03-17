package com.projetm1.pizzeria.chat;

import com.projetm1.pizzeria.compte.Compte;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "expediteur_id")
    private Compte expediteur;

    @ManyToOne
    @JoinColumn(name = "receveur_id")
    private Compte receveur;

    private String message;
    private LocalDateTime dateEnvoi;
}
