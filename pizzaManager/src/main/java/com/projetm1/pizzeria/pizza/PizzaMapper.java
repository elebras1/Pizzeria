package com.projetm1.pizzeria.pizza;

import com.projetm1.pizzeria.pizza.dto.PizzaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PizzaMapper {

    Pizza toEntity(PizzaDto dto);

    PizzaDto toDto(Pizza entity);
}
