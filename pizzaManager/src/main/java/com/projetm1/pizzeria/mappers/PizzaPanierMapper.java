package com.projetm1.pizzeria.mappers;

import com.projetm1.pizzeria.dtos.PizzaPanierDto;
import com.projetm1.pizzeria.entities.PizzaPanier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PizzaMapper.class, IngredientMapper.class})
public interface PizzaPanierMapper {

    @Mapping(source = "idPanier", target = "commande.id")
    PizzaPanier toEntity(PizzaPanierDto dto);

    @Mapping(source = "commande.id", target = "idPanier")
    PizzaPanierDto toDto(PizzaPanier entity);
}
