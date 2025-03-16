package com.projetm1.pizzeria.pizza;

import com.projetm1.pizzeria.ingredient.IngredientMapper;
import com.projetm1.pizzeria.pizza.dto.PizzaDto;
import com.projetm1.pizzeria.pizza.dto.PizzaLightDto;
import com.projetm1.pizzeria.pizza.dto.PizzaRequestDto;
import com.projetm1.pizzeria.ingredient.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = { IngredientMapper.class })
public interface PizzaMapper {

    PizzaDto toDto(Pizza pizza);

    PizzaLightDto toLightDto(Pizza pizza);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "standardIngredients", expression = "java(mapIngredientIdsToIngredients(pizzaRequestDto.getStandardIngredientsIds()))")
    @Mapping(target = "optionalIngredients", expression = "java(mapIngredientIdsToIngredients(pizzaRequestDto.getOptionalIngredientsIds()))")
    @Mapping(target = "pizzaPaniers", ignore = true)
    Pizza toEntity(PizzaRequestDto pizzaRequestDto);

    default Ingredient mapIngredientId(Long id) {
        if (id == null) {
            return null;
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        return ingredient;
    }

    default Set<Ingredient> mapIngredientIdsToIngredients(Set<Long> ids) {
        if (ids == null) {
            return null;
        }
        return ids.stream().map(this::mapIngredientId).collect(Collectors.toSet());
    }
}
