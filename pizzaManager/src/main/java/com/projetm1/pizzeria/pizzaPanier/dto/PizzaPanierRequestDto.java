package com.projetm1.pizzeria.pizzaPanier.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PizzaPanierRequestDto {
    private Long pizzaId;
    private Set<Long> ingredientsIds;
}
