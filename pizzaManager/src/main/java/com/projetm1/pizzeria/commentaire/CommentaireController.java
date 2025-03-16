package com.projetm1.pizzeria.commentaire;

import com.projetm1.pizzeria.commentaire.dto.CommentaireDto;
import com.projetm1.pizzeria.commentaire.dto.CommentaireRequestDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commentaires")
public class CommentaireController {
    private final CommentaireService commentaireService;

    public CommentaireController(CommentaireService commentaireService) {
        this.commentaireService = commentaireService;
    }

    @GetMapping
    public List<CommentaireDto> getAllCommentaires() {
        return this.commentaireService.getAllCommentaires();
    }

    @GetMapping("/{id}")
    public CommentaireDto getCommentaireById(@PathVariable String id) {
        return this.commentaireService.getCommentaireById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCommentaireById(@PathVariable String id) {
        this.commentaireService.deleteCommentaireById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommentaireDto updateCommentaire(@PathVariable String id, @ModelAttribute CommentaireRequestDto commentaireDto) {
        return this.commentaireService.updateCommentaire(id, commentaireDto);
    }
}
