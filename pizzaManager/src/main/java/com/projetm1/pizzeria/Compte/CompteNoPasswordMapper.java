package com.projetm1.pizzeria.Compte;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompteNoPasswordMapper {

    Compte toEntity(CompteNoPasswordDto dto);

    CompteNoPasswordDto toDto(Compte entity);
}
