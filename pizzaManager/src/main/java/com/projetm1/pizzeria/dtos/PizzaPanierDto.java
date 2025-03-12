package com.projetm1.pizzeria.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PizzaPanierDto {
    private Long id;
    private PizzaDto pizza;
    private List<IngredientDto> ingredients;
    private Long idPanier;
}
