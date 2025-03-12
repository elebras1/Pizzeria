package com.projetm1.pizzeria.dtos;

import lombok.Data;

@Data
public class CompteDto {
    private Long id;
    private String pseudo;
    private String prenom;
    private String nom;
    private String motDePasse;
}
