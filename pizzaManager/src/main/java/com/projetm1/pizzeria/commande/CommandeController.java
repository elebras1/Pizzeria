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
    private final CommandeMapper commandeMapper;
    private final CommandeRepository commandeRepository;

    public CommandeController(CommandeService commandeService, CommentaireService commentaireService, CommentaireService commentaireService1, CommandeMapper commandeMapper, CommandeRepository commandeRepository) {
        this.commandeService = commandeService;
        this.commentaireService = commentaireService1;
        this.commandeMapper = commandeMapper;
        this.commandeRepository = commandeRepository;
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
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compte = objectMapper.readValue(compteJson, CompteDto.class);
            Optional<Commande> commande=this.commandeService.getCommandeEnCoursByCompteId(compte.getId());
            return commande.map(this.commandeMapper::toDto).orElse(null);
        }catch (JsonProcessingException e){
            return null;
        }
    }


    @PostMapping
    public CommandeDto saveCommande(@RequestHeader("x-compte") String compteJson,@RequestBody CommandeRequestDto commandeDto) {
        try{
            System.out.println(compteJson);
            System.out.println(commandeDto);
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compte = objectMapper.readValue(compteJson, CompteDto.class);
            commandeDto.setCompteId(compte.getId());
            Optional<Commande> commandeEnCours = this.commandeService.getCommandeEnCoursByCompteId(compte.getId());
            System.out.println(commandeEnCours.isEmpty());
            if(commandeEnCours.isPresent()){
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
            Optional<Commande> commande = this.commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(commandeDto.getCompteId());
            if(commande.isEmpty()){
                return null;
            }
            System.out.println("test");
            return this.commandeService.updateCommande(commandeDto,commande.get());
        }catch (JsonProcessingException e){
            return null;
        }
    }
    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestHeader("x-compte") String compteJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compte = objectMapper.readValue(compteJson, CompteDto.class);
            Optional<Commande> commande = this.commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compte.getId());
            if(commande.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Float total = 0.0f;
            for(PizzaPanier pizzaPanier: commande.get().getPanier()){
                for(Ingredient ingredient: pizzaPanier.getIngredients()){
                    total = total+ ingredient.getPrix();
                }
            }

            System.out.println(total);
            long montantEnCentimes = (long) (total * 100L);
            if(montantEnCentimes == 0){
                Map<String, String> response = new HashMap<>();
                response.put("url","http://localhost:5173/panier" );
                return ResponseEntity.ok(response);
            }
            System.out.println(montantEnCentimes);
            Stripe.apiKey = "sk_test_51R3PKZQxmuo6VLbo318TeqOwaacBuCiV8c4xGEXvqWT43qLtbkpVAkjuuKsfly5xvaoyfSwvE0PqmZJENBjXjaax00duyJFo1M";

            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName("Commande de pizzas")
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("eur")
                            .setUnitAmount(montantEnCentimes)
                            .setProductData(productData)
                            .build();

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(priceData)
                            .build();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:5173/payment/sucess")
                    .setCancelUrl("http://localhost:5173/payment/reject")
                    .addLineItem(lineItem)
                    .build();

            Session session = Session.create(params);


            Map<String, String> response = new HashMap<>();
            response.put("url", session.getUrl());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/paySuccess")
    public Boolean payerSuccess(@RequestHeader("x-compte") String compteJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compte = objectMapper.readValue(compteJson, CompteDto.class);
            Optional<Commande> commande = this.commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compte.getId());
            return commande.map(value -> this.commandeService.payCommande(value.getId())).orElse(null);
        }catch (JsonProcessingException e){
            return null;
        }
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
