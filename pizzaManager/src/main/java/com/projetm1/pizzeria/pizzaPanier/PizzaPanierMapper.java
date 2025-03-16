package com.projetm1.pizzeria.pizzaPanier;

import com.projetm1.pizzeria.ingredient.IngredientMapper;
import com.projetm1.pizzeria.pizza.PizzaMapper;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierDto;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierRequestDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PizzaMapper.class, IngredientMapper.class})
public interface PizzaPanierMapper {

    PizzaPanierDto toDto(PizzaPanier pizzaPanier);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "commande", ignore = true)
    @Mapping(target = "pizza", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    PizzaPanier toEntity(PizzaPanierRequestDto dto);

    List<PizzaPanier> toEntityList(List<PizzaPanierRequestDto> dtos);

    List<PizzaPanierRequestDto> toDtoList(List<PizzaPanier> entities);
}
