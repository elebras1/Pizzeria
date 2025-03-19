package com.projetm1.pizzeria.commande;

import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.commande.dto.CommandeRequestDto;
import com.projetm1.pizzeria.commentaire.CommentaireService;
import com.projetm1.pizzeria.commentaire.dto.CommentaireDto;
import com.projetm1.pizzeria.commentaire.dto.CommentaireRequestDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {
    private final CommandeService commandeService;
    private final CommentaireService commentaireService;

    public CommandeController(CommandeService commandeService,CommentaireService commentaireService1) {
        this.commandeService = commandeService;
        this.commentaireService = commentaireService1;
    }

    @GetMapping
    public ResponseEntity<List<CommandeDto>> getAllCommandes() {
        return ResponseEntity.ok(this.commandeService.getAllCommandes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDto> getCommandeById(@PathVariable Long id) {
        return ResponseEntity.ok(this.commandeService.getCommandeById(id));
    }

    @GetMapping("/enCours")
    public ResponseEntity<CommandeDto> getCommandeEnCoursByCompteId(@RequestHeader("x-compte") String compteJson) {
        return ResponseEntity.ok(this.commandeService.getCommandeEnCoursByCompteId(compteJson));
    }

    @PostMapping
    public ResponseEntity<CommandeDto> saveCommande(@RequestHeader("x-compte") String compteJson,@RequestBody CommandeRequestDto commandeDto) {
        return ResponseEntity.ok(this.commandeService.saveCommande(compteJson,commandeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommandeById(@PathVariable Long id) {
        this.commandeService.deleteCommandeById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<CommandeDto> updateCommande(@RequestHeader("x-compte") String compteJson, @RequestBody CommandeRequestDto commandeDto) {
        return ResponseEntity.ok(this.commandeService.updateCommande(compteJson, commandeDto));
    }

    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestHeader("x-compte") String compteJson) {
        return this.commandeService.createCheckoutSession(compteJson);
    }

    @PutMapping("/paySuccess")
    public Boolean payerSuccess(@RequestHeader("x-compte") String compteJson) {
        return this.commandeService.payerSuccess(compteJson);
    }

    @PostMapping(value = "/{id}/commentaires", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommentaireDto> addCommentaireToCommande(@PathVariable Long id, @ModelAttribute CommentaireRequestDto commentaireDto) {
        return ResponseEntity.ok(this.commentaireService.saveCommentaire(id, commentaireDto));
    }

    @GetMapping("/{id}/commentaires")
    public ResponseEntity<List<CommentaireDto>> getCommentairesByCommandeId(@PathVariable Long id) {
        return ResponseEntity.ok(this.commentaireService.getCommentairesByCommandeId(id));
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<CommandeDto> finishCommande(@PathVariable Long id) {
        return ResponseEntity.ok(this.commandeService.finishCommande(id));
    }
}
