package com.projetm1.pizzeria.compte;

import com.projetm1.pizzeria.compte.dto.CompteDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompteMapper {

    Compte toEntity(CompteDto dto);

    CompteDto toDto(Compte entity);
}
