package com.projetm1.pizzeria.mappers;

import com.projetm1.pizzeria.dtos.PizzaDto;
import com.projetm1.pizzeria.entities.Pizza;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PizzaMapper {

    Pizza toEntity(PizzaDto dto);

    PizzaDto toDto(Pizza entity);
}
