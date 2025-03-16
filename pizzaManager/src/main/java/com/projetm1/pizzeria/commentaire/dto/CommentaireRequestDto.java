package com.projetm1.pizzeria.commentaire.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CommentaireRequestDto {
    private String texte;
    private MultipartFile photo;
}
