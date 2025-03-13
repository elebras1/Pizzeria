package com.projetm1.pizzeria.ingredient.dto;

import lombok.Data;

@Data
public class IngredientRequestDto {
    private String nom;
    private String description;
    private Float prix;
}
