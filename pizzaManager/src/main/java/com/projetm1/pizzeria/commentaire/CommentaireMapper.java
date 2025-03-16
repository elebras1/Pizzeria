package com.projetm1.pizzeria.commentaire;

import com.projetm1.pizzeria.commentaire.dto.CommentaireDto;
import com.projetm1.pizzeria.commentaire.dto.CommentaireRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentaireMapper {

    CommentaireDto toDto(Commentaire commentaire);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idCommande", ignore = true)
    @Mapping(target = "photo", ignore = true)
    Commentaire toEntity(CommentaireRequestDto commentaireRequestDto);
}
