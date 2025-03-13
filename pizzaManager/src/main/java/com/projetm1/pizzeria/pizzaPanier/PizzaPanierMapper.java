package com.projetm1.pizzeria.pizzaPanier;

import com.projetm1.pizzeria.ingredient.IngredientMapper;
import com.projetm1.pizzeria.pizza.PizzaMapper;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PizzaMapper.class, IngredientMapper.class})
public interface PizzaPanierMapper {

    @Mapping(source = "idPanier", target = "commande.id")
    PizzaPanier toEntity(PizzaPanierDto dto);

    @Mapping(source = "commande.id", target = "idPanier")
    PizzaPanierDto toDto(PizzaPanier entity);
}
