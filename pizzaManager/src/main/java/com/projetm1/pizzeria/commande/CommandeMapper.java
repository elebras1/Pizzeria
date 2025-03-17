package com.projetm1.pizzeria.commande;

import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.commande.dto.CommandeRequestDto;
import com.projetm1.pizzeria.compte.CompteMapper;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanier;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanierMapper;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierRequestDto;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CompteMapper.class, PizzaPanierMapper.class})
public interface CommandeMapper {

    @Mapping(source = "compte.id", target = "compteId")
    @Mapping(source = "idCommentaires", target = "commentairesIds")
    CommandeDto toDto(Commande commande);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "compteId", target = "compte.id")
    @Mapping(source = "commentairesIds", target = "idCommentaires")
    Commande toEntity(CommandeRequestDto commandeRequestDto);

}
