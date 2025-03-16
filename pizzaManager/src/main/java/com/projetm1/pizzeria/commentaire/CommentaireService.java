package com.projetm1.pizzeria.commentaire;

import com.projetm1.pizzeria.commande.Commande;
import com.projetm1.pizzeria.commande.CommandeRepository;
import com.projetm1.pizzeria.commentaire.dto.CommentaireDto;
import com.projetm1.pizzeria.commentaire.dto.CommentaireRequestDto;
import com.projetm1.pizzeria.image.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("commentaireService")
@Transactional
public class CommentaireService {
    private final CommentaireRepository commentaireRepository;
    private final CommentaireMapper commentaireMapper;
    private final CommandeRepository commandeRepository;
    private final ImageService imageService;

    public CommentaireService(CommentaireRepository commentaireRepository, CommentaireMapper commentaireMapper, CommandeRepository commandeRepository, ImageService imageService) {
        this.commentaireRepository = commentaireRepository;
        this.commentaireMapper = commentaireMapper;
        this.commandeRepository = commandeRepository;
        this.imageService = imageService;
    }

    public CommentaireDto getCommentaireById(String id) {
        return this.commentaireMapper.toDto(this.commentaireRepository.findById(id).orElse(null));
    }

    public List<CommentaireDto> getAllCommentaires() {
        return this.commentaireRepository.findAll().stream().map(this.commentaireMapper::toDto).collect(Collectors.toList());
    }

    public CommentaireDto saveCommentaire(Long commandeId, CommentaireRequestDto commentaireDto) {
        Commande commande = this.commandeRepository.findById(commandeId).orElseThrow();

        String fileName = this.imageService.saveImage(commentaireDto.getPhoto());

        Commentaire commentaire = this.commentaireMapper.toEntity(commentaireDto);
        commentaire.setIdCommande(commandeId.toString());
        if(fileName != null) {
            commentaire.setPhoto(fileName);
        }
        commentaire = this.commentaireRepository.save(commentaire);

        List<String> commentairesIds = commande.getIdCommentaires();
        if (commentairesIds == null) {
            commentairesIds = new ArrayList<>();
            commande.setIdCommentaires(commentairesIds);
        }
        commentairesIds.add(commentaire.getId());
        this.commandeRepository.save(commande);

        return this.commentaireMapper.toDto(commentaire);
    }

    public void deleteCommentaireById(String id) {
        Commentaire commentaire = this.commentaireRepository.findById(id).orElse(null);

        if (commentaire != null && commentaire.getIdCommande() != null) {
            try {
                Long cmdId = Long.parseLong(commentaire.getIdCommande());
                Commande commande = this.commandeRepository.findById(cmdId).orElse(null);

                if (commande != null && commande.getIdCommentaires() != null) {
                    commande.getIdCommentaires().remove(id);
                    this.commandeRepository.save(commande);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        this.commentaireRepository.deleteById(id);
    }

    public CommentaireDto updateCommentaire(String id, CommentaireRequestDto commentaireDto) {
        if(!this.commentaireRepository.existsById(id)) {
            return null;
        }

        String fileName = this.imageService.saveImage(commentaireDto.getPhoto());

        Commentaire existingCommentaire = this.commentaireRepository.findById(id).orElse(null);
        if (existingCommentaire == null) {
            return null;
        }

        Commentaire commentaire = this.commentaireMapper.toEntity(commentaireDto);
        commentaire.setId(id);
        commentaire.setIdCommande(existingCommentaire.getIdCommande());
        if (fileName != null) {
            commentaire.setPhoto(fileName);
        } else {
            commentaire.setPhoto(existingCommentaire.getPhoto());
        }

        return this.commentaireMapper.toDto(this.commentaireRepository.save(commentaire));
    }

    public List<CommentaireDto> getCommentairesByCommandeId(Long commandeId) {
        Commande commande = this.commandeRepository.findById(commandeId).orElse(null);
        if(commande == null || commande.getIdCommentaires() == null || commande.getIdCommentaires().isEmpty()) {
            return new ArrayList<>();
        }

        List<Commentaire> commentaires = new ArrayList<>();
        for(String commentaireId : commande.getIdCommentaires()) {
            Commentaire commentaire = this.commentaireRepository.findById(commentaireId).orElse(null);
            if(commentaire != null) {
                commentaires.add(commentaire);
            }
        }

        return commentaires.stream().map(this.commentaireMapper::toDto).collect(Collectors.toList());
    }
}
