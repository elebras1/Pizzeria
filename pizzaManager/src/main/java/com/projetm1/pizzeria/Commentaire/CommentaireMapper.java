package com.projetm1.pizzeria.Commentaire;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentaireMapper {

    @Mapping(target = "idCommande", expression = "java(dto.getIdCommande() != null ? dto.getIdCommande().toString() : null)")
    Commentaire toEntity(CommentaireDto dto);

    @Mapping(target = "idCommande", expression = "java(entity.getIdCommande() != null ? Long.valueOf(entity.getIdCommande()) : null)")
    CommentaireDto toDto(Commentaire entity);
}
