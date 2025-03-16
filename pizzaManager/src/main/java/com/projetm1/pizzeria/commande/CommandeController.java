package com.projetm1.pizzeria.commande;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.commande.dto.CommandeRequestDto;
import com.projetm1.pizzeria.commentaire.CommentaireService;
import com.projetm1.pizzeria.commentaire.dto.CommentaireDto;
import com.projetm1.pizzeria.commentaire.dto.CommentaireRequestDto;
import com.projetm1.pizzeria.compte.Compte;
import com.projetm1.pizzeria.compte.CompteService;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {
    private final CommandeService commandeService;
    private final CommentaireService commentaireService;
    private final CompteService compteService;

    public CommandeController(CommandeService commandeService, CommentaireService commentaireService, CommentaireService commentaireService1, CompteService compteService) {
        this.commandeService = commandeService;
        this.commentaireService = commentaireService1;
        this.compteService = compteService;
    }

    @GetMapping
    public List<CommandeDto> getAllCommandes() {
        return this.commandeService.getAllCommandes();
    }

    @GetMapping("/{id}")
    public CommandeDto getCommandeById(@PathVariable Long id) {
        return this.commandeService.getCommandeById(id);
    }

    @PostMapping
    public CommandeDto saveCommande(@RequestHeader("x-compte") String compteJson,@RequestBody CommandeRequestDto commandeDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compte = objectMapper.readValue(compteJson, CompteDto.class);
            commandeDto.setCompteId(compte.getId());
            return this.commandeService.saveCommande(commandeDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCommandeById(@PathVariable Long id) {
        this.commandeService.deleteCommandeById(id);
    }

    @PutMapping
    public CommandeDto updateCommande(@RequestBody CommandeRequestDto commandeDto,@RequestHeader(value = "x-compte", required = true) String compteJson) {
        //ObjectMapper objectMapper = new ObjectMapper();
        //CompteDto compteDto = objectMapper.readValue(compteJson, CompteDto.class);
        //commandeDto.setCompteId(compteDto.getId());
        //Long commandeId=this.commandeService.getLastCommandeIdByCompteId(compteDto.getId());
        //return this.commandeService.updateCommande(commandeId, commandeDto);
        return null;
    }

    @PostMapping("/{id}/commentaires")
    public CommentaireDto addCommentaireToCommande(@PathVariable String id, @RequestBody CommentaireRequestDto commentaireDto) {
        return this.commentaireService.saveCommentaire(id, commentaireDto);
    }

    @GetMapping("/{id}/commentaires")
    public List<CommentaireDto> getCommentairesByCommandeId(@PathVariable Long id) {
        return this.commentaireService.getCommentairesByCommandeId(id);
    }
}
