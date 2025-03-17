package com.projetm1.pizzeria.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Récupérer les messages entre deux comptes (expéditeur et receveur) dans l'ordre chronologique
    List<ChatMessage> findByExpediteurIdAndReceveurIdOrderByDateEnvoiAsc(Long expediteurId, Long receveurId);

    // Récupérer les messages échangés dans les deux sens (expéditeur -> receveur et receveur -> expéditeur)
    List<ChatMessage> findByExpediteurIdAndReceveurIdOrReceveurIdAndExpediteurIdOrderByDateEnvoiAsc(
            Long expediteurId1, Long receveurId1, Long expediteurId2, Long receveurId2
    );
}
