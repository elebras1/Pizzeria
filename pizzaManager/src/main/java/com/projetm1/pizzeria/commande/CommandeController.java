package com.projetm1.pizzeria.commande;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.commande.dto.CommandeRequestDto;
import com.projetm1.pizzeria.commentaire.CommentaireService;
import com.projetm1.pizzeria.commentaire.dto.CommentaireDto;
import com.projetm1.pizzeria.commentaire.dto.CommentaireRequestDto;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import com.projetm1.pizzeria.ingredient.Ingredient;
import com.projetm1.pizzeria.ingredient.dto.IngredientDto;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public List<CommandeDto> getAllCommandes() {
        return this.commandeService.getAllCommandes();
    }

    @GetMapping("/{id}")
    public CommandeDto getCommandeById(@PathVariable Long id) {
        return this.commandeService.getCommandeById(id);
    }

    @GetMapping("/enCours")
    public CommandeDto getCommandeEnCoursByCompteId(@RequestHeader("x-compte") String compteJson) {
        return this.commandeService.getCommandeEnCoursByCompteId(compteJson);
    }

    @PostMapping
    public CommandeDto saveCommande(@RequestHeader("x-compte") String compteJson,@RequestBody CommandeRequestDto commandeDto) {
        return this.commandeService.saveCommande(compteJson,commandeDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCommandeById(@PathVariable Long id) {
        this.commandeService.deleteCommandeById(id);
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
    public CommentaireDto addCommentaireToCommande(@PathVariable Long id, @ModelAttribute CommentaireRequestDto commentaireDto) {
        return this.commentaireService.saveCommentaire(id, commentaireDto);
    }

    @GetMapping("/{id}/commentaires")
    public List<CommentaireDto> getCommentairesByCommandeId(@PathVariable Long id) {
        return this.commentaireService.getCommentairesByCommandeId(id);
    }

    @PutMapping("/{id}/finish")
    public CommandeDto finishCommande(@PathVariable Long id) {
        return this.commandeService.finishCommande(id);
    }
}
