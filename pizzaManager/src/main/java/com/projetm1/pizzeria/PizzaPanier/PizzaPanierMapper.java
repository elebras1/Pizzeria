package com.projetm1.pizzeria.PizzaPanier;

import com.projetm1.pizzeria.Ingredient.IngredientMapper;
import com.projetm1.pizzeria.Pizza.PizzaMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PizzaMapper.class, IngredientMapper.class})
public interface PizzaPanierMapper {

    @Mapping(source = "idPanier", target = "commande.id")
    PizzaPanier toEntity(PizzaPanierDto dto);

    @Mapping(source = "commande.id", target = "idPanier")
    PizzaPanierDto toDto(PizzaPanier entity);
}
