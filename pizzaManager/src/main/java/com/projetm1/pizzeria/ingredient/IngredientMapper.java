package com.projetm1.pizzeria.ingredient;

import com.projetm1.pizzeria.ingredient.dto.IngredientDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    Ingredient toEntity(IngredientDto dto);

    IngredientDto toDto(Ingredient entity);
}
