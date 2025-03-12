package com.projetm1.pizzeria.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "commentaire")
public class Commentaire {
    @Id
    private String id;
    private String texte;
    private String photo;
    private String idCommande;
}

