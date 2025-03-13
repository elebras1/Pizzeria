package com.projetm1.pizzeria.PizzaPanier;

import com.projetm1.pizzeria.Ingredient.IngredientDto;
import com.projetm1.pizzeria.Pizza.PizzaDto;
import lombok.Data;

import java.util.List;

@Data
public class PizzaPanierDto {
    private Long id;
    private PizzaDto pizza;
    private List<IngredientDto> ingredients;
    private Long idPanier;
}
