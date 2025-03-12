package com.projetm1.pizzeria.mappers;

import com.projetm1.pizzeria.dtos.CommandeDto;
import com.projetm1.pizzeria.entities.Commande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PizzaPanierMapper.class})
public interface CommandeMapper {

    @Mapping(source = "idCompte", target = "compte.id")
    Commande toEntity(CommandeDto dto);

    @Mapping(source = "compte.id", target = "idCompte")
    CommandeDto toDto(Commande entity);
}
