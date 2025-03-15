package com.projetm1.pizzeria.commande.dto;

import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierDto;
import lombok.Data;

import java.util.List;

@Data
public class CommandeRequestDto {
    private Long compteId;
    private Boolean enCours;
    private List<PizzaPanierDto> panier;
    private List<String> commentairesIds;
}
