package com.projetm1.pizzeria.pizza.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PizzaRequestDto {
    private String nom;
    private String description;
    private String photo;
    private Set<Long> standardIngredientsIds;
    private Set<Long> optionalIngredientsIds;
}
