package com.projetm1.pizzeria.commande.dto;

import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommandeDto {
    private Long id;
    private Long compteId;
    private Boolean enCours;
    private Boolean isPaye;
    private LocalDateTime date;
    private List<PizzaPanierDto> panier;
    private List<String> commentairesIds;
}
