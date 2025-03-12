package com.projetm1.pizzeria.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CommandeDto {
    private Long id;
    private Long idCompte;
    private List<PizzaPanierDto> panier;
}
