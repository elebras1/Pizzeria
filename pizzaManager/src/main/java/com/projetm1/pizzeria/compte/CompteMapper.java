package com.projetm1.pizzeria.compte;

import com.projetm1.pizzeria.compte.dto.CompteDto;
import com.projetm1.pizzeria.compte.dto.CompteRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompteMapper {

    CompteDto toDto(Compte compte);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "commandes", ignore = true)
    Compte toEntity(CompteRequestDto compteRequestDto);
}
