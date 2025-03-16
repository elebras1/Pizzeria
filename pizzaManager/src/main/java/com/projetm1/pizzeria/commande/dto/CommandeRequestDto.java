package com.projetm1.pizzeria.commande.dto;

import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierDto;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierRequestDto;
import lombok.Data;

import java.util.List;

@Data
public class CommandeRequestDto {
    private Long compteId;
    private Boolean enCours;
    private List<PizzaPanierRequestDto> panier;
    private List<String> commentairesIds;
}
