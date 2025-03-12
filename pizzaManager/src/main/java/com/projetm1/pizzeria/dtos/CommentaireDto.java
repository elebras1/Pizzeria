package com.projetm1.pizzeria.dtos;

import lombok.Data;

@Data
public class CommentaireDto {
    private String id;
    private String texte;
    private String photo;
    private Long idCommande;
}
