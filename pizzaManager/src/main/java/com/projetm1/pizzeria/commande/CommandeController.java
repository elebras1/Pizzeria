package com.projetm1.pizzeria.commande;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.commande.dto.CommandeRequestDto;
import com.projetm1.pizzeria.commentaire.CommentaireService;
import com.projetm1.pizzeria.commentaire.dto.CommentaireDto;
import com.projetm1.pizzeria.commentaire.dto.CommentaireRequestDto;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {
    private final CommandeService commandeService;
    private final CommentaireService commentaireService;

    public CommandeController(CommandeService commandeService, CommentaireService commentaireService, CommentaireService commentaireService1) {
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

    @PostMapping
    public CommandeDto saveCommande(@RequestHeader("x-compte") String compteJson,@RequestBody CommandeRequestDto commandeDto) {
        try{
            System.out.println(compteJson);
            System.out.println(commandeDto);
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compte = objectMapper.readValue(compteJson, CompteDto.class);
            commandeDto.setCompteId(compte.getId());
            Commande commandeEnCours = this.commandeService.getCommandeEnCoursByCompteId(compte.getId());
            if(commandeEnCours != null){
                return null;
            }
            return this.commandeService.saveCommande(commandeDto);
        }catch (JsonProcessingException e){
            return  null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCommandeById(@PathVariable Long id) {
        this.commandeService.deleteCommandeById(id);
    }

    @PutMapping
    public CommandeDto updateCommande(@RequestHeader("x-compte") String compteJson,@RequestBody CommandeRequestDto commandeDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compte = objectMapper.readValue(compteJson, CompteDto.class);
            commandeDto.setCompteId(compte.getId());
            return this.commandeService.updateCommande(commandeDto);
        }catch (JsonProcessingException e){
            return null;
        }
    }

    @PostMapping(value = "/{id}/commentaires", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommentaireDto addCommentaireToCommande(@PathVariable Long id, @RequestBody CommentaireRequestDto commentaireDto) {
        return this.commentaireService.saveCommentaire(id, commentaireDto);
    }

    @GetMapping("/{id}/commentaires")
    public List<CommentaireDto> getCommentairesByCommandeId(@PathVariable Long id) {
        return this.commentaireService.getCommentairesByCommandeId(id);
    }
}
