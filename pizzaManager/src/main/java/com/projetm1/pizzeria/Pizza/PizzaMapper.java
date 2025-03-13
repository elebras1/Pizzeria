package com.projetm1.pizzeria.Pizza;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PizzaMapper {

    Pizza toEntity(PizzaDto dto);

    PizzaDto toDto(Pizza entity);
}
