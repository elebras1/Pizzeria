package com.projetm1.pizzeria.ingredient;

import com.projetm1.pizzeria.ingredient.dto.IngredientDto;
import com.projetm1.pizzeria.ingredient.dto.IngredientRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    IngredientDto toDto(Ingredient ingredient);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pizzasStandard", ignore = true)
    @Mapping(target = "pizzasOptionnel", ignore = true)
    @Mapping(target = "pizzaPaniers", ignore = true)
    Ingredient toEntity(IngredientRequestDto ingredientRequestDto);
}
