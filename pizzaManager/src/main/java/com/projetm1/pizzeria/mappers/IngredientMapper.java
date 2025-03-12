package com.projetm1.pizzeria.mappers;

import com.projetm1.pizzeria.dtos.IngredientDto;
import com.projetm1.pizzeria.entities.Ingredient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    Ingredient toEntity(IngredientDto dto);

    IngredientDto toDto(Ingredient entity);
}
