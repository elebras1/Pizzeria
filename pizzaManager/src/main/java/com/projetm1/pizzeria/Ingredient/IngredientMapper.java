package com.projetm1.pizzeria.Ingredient;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    Ingredient toEntity(IngredientDto dto);

    IngredientDto toDto(Ingredient entity);
}
