package com.projetm1.pizzeria.Commande;

import com.projetm1.pizzeria.PizzaPanier.PizzaPanierMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PizzaPanierMapper.class})
public interface CommandeMapper {

    @Mapping(source = "idCompte", target = "compte.id")
    Commande toEntity(CommandeDto dto);

    @Mapping(source = "compte.id", target = "idCompte")
    CommandeDto toDto(Commande entity);
}
