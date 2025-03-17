package com.projetm1.pizzeria.commande.dto;

import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierRequestDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommandeRequestDto {
    private Long compteId;
    private List<PizzaPanierRequestDto> panier;
    private List<String> commentairesIds;
}
