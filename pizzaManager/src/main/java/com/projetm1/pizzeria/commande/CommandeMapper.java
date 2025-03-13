package com.projetm1.pizzeria.commande;

import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanierMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PizzaPanierMapper.class})
public interface CommandeMapper {

    @Mapping(source = "idCompte", target = "compte.id")
    Commande toEntity(CommandeDto dto);

    @Mapping(source = "compte.id", target = "idCompte")
    CommandeDto toDto(Commande entity);
}
