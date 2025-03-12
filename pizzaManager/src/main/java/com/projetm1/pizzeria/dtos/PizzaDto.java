package com.projetm1.pizzeria.dtos;

import lombok.Data;

@Data
public class PizzaDto {
    private Long id;
    private String nom;
    private String description;
    private String photo;
}
