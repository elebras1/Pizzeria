package com.projetm1.pizzeria.compte.dto;

import lombok.Data;

@Data
public class CompteRequestDto {
    private String pseudo;
    private String prenom;
    private String nom;
    private String motDePasse;
    private Boolean isAdmin;
}
