package com.projetm1.pizzeria.pizza.dto;

import com.projetm1.pizzeria.ingredient.dto.IngredientDto;
import lombok.Data;

import java.util.Set;

@Data
public class PizzaDto {
    private Long id;
    private String nom;
    private String description;
    private String photo;
    private Set<IngredientDto> standardIngredients;
    private Set<IngredientDto> optionalIngredients;
}
