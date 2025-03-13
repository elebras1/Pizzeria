package com.projetm1.pizzeria.commentaire;

import com.projetm1.pizzeria.commentaire.dto.CommentaireDto;
import com.projetm1.pizzeria.commentaire.dto.CommentaireRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("commentaireService")
@Transactional
public class CommentaireService {
    private final CommentaireRepository commentaireRepository;
    private final CommentaireMapper commentaireMapper;

    public CommentaireService(CommentaireRepository commentaireRepository, CommentaireMapper commentaireMapper) {
        this.commentaireRepository = commentaireRepository;
        this.commentaireMapper = commentaireMapper;
    }

    public CommentaireDto getCommentaireById(String id) {
        return this.commentaireMapper.toDto(this.commentaireRepository.findById(id).orElse(null));
    }

    public List<CommentaireDto> getAllCommentaires() {
        return this.commentaireRepository.findAll().stream().map(this.commentaireMapper::toDto).collect(Collectors.toList());
    }

    public CommentaireDto saveCommentaire(String commandeId, CommentaireRequestDto commentaireDto) {
        Commentaire commentaire = this.commentaireMapper.toEntity(commentaireDto);
        commentaire.setIdCommande(commandeId);
        commentaire = this.commentaireRepository.save(commentaire);

        return this.commentaireMapper.toDto(commentaire);
    }

    public void deleteCommentaireById(String id) {
        this.commentaireRepository.deleteById(id);
    }

    public CommentaireDto updateCommentaire(String id, CommentaireRequestDto commentaireDto) {
        if(!this.commentaireRepository.existsById(id)) {
            return null;
        }

        Commentaire commentaire = this.commentaireMapper.toEntity(commentaireDto);
        commentaire.setId(id);

        return this.commentaireMapper.toDto(this.commentaireRepository.save(commentaire));
    }
}
