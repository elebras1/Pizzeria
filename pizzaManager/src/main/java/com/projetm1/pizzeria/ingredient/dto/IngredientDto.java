package com.projetm1.pizzeria.ingredient.dto;

import lombok.Data;

@Data
public class IngredientDto {
    private Long id;
    private String nom;
    private String description;
    private Float prix;
}
