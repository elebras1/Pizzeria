package com.projetm1.pizzeria.mappers;

import com.projetm1.pizzeria.dtos.CompteDto;
import com.projetm1.pizzeria.entities.Compte;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompteMapper {

    Compte toEntity(CompteDto dto);

    CompteDto toDto(Compte entity);
}
