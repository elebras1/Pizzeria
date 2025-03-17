package com.projetm1.pizzeria.pizzaPanier.dto;

import com.projetm1.pizzeria.ingredient.dto.IngredientDto;
import com.projetm1.pizzeria.pizza.dto.PizzaLightDto;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class PizzaPanierDto {
    private Long id;
    private PizzaLightDto pizza;
    private List<IngredientDto> ingredients;
}
