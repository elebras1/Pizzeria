package com.projetm1.pizzeria.Compte;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompteMapper {

    Compte toEntity(CompteDto dto);

    CompteDto toDto(Compte entity);
}
