package com.projetm1.pizzeria.commentaire;

import com.projetm1.pizzeria.commentaire.dto.CommentaireDto;
import com.projetm1.pizzeria.commentaire.dto.CommentaireRequestDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<CommentaireDto>> getAllCommentaires() {
        return ResponseEntity.ok(this.commentaireService.getAllCommentaires());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentaireDto> getCommentaireById(@PathVariable String id) {
        return ResponseEntity.ok(this.commentaireService.getCommentaireById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentaireById(@PathVariable String id) {
        this.commentaireService.deleteCommentaireById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommentaireDto> updateCommentaire(@PathVariable String id, @ModelAttribute CommentaireRequestDto commentaireDto) {
        return ResponseEntity.ok(this.commentaireService.updateCommentaire(id, commentaireDto));
    }
}
