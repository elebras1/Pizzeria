package com.projetm1.pizzeria.commande;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.commande.dto.CommandeRequestDto;
import com.projetm1.pizzeria.compte.Compte;
import com.projetm1.pizzeria.compte.CompteRepository;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import com.projetm1.pizzeria.error.NotFound;
import com.projetm1.pizzeria.ingredient.Ingredient;
import com.projetm1.pizzeria.ingredient.IngredientRepository;
import com.projetm1.pizzeria.pizza.Pizza;
import com.projetm1.pizzeria.pizza.PizzaRepository;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanier;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierRequestDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("commandeService")
@Transactional
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final CommandeMapper commandeMapper;
    private final CompteRepository compteRepository;
    private final PizzaRepository pizzaRepository;
    private final IngredientRepository ingredientRepository;

    public CommandeService(CommandeRepository commandeRepository, CommandeMapper commandeMapper, CompteRepository compteRepository, PizzaRepository pizzaRepository, IngredientRepository ingredientRepository) {
        this.commandeRepository = commandeRepository;
        this.commandeMapper = commandeMapper;
        this.compteRepository = compteRepository;
        this.pizzaRepository = pizzaRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public CommandeDto getCommandeById(Long id) {
        return this.commandeMapper.toDto(this.commandeRepository.findById(id).orElseThrow(() -> new NotFound("Commande introuvable")));
    }

    public List<CommandeDto> getAllCommandes() {
        return this.commandeRepository.findAll().stream().map(this.commandeMapper::toDto).collect(Collectors.toList());
    }

    public CommandeDto saveCommande(String compteJson,CommandeRequestDto commandeDto) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compteDto = objectMapper.readValue(compteJson, CompteDto.class);
            Optional<Commande> commandeEnCours = this.getCommandeEnCoursByCompteId(compteDto.getId());
            if(commandeEnCours.isPresent()){
                throw new NotFound("Commande en cours déjà existante");
            }
            commandeDto.setCompteId(compteDto.getId());
            Commande commande = this.commandeMapper.toEntity(commandeDto);
            Compte compte = this.compteRepository.findById(commandeDto.getCompteId()).orElseThrow(() -> new NotFound("Compte introuvable"));
            commande.setCompte(compte);
            commande.setEnCours(true);
            commande.setIsPaye(false);
            commande.setDate(LocalDateTime.now());
            commande.setIdCommentaires(new ArrayList<>());
            return updatePanier(commandeDto, commande);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Erreur lors de la création de la commande");
        }
    }

    private Optional<Commande> getCommandeEnCoursByCompteId(Long compteId) {
        return this.commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compteId);
    }

    public CommandeDto getCommandeEnCoursByCompteId(String compteJson) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compte = objectMapper.readValue(compteJson, CompteDto.class);
            Optional<Commande> commande=this.getCommandeEnCoursByCompteId(compte.getId());
            return commande.map(this.commandeMapper::toDto).orElse(null);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Erreur lors de la récupération de la commande");
        }
    }

    public void deleteCommandeById(Long id) {
        this.commandeRepository.findById(id).orElseThrow(() -> new NotFound("Commande introuvable"));
        this.commandeRepository.deleteById(id);
    }

    public CommandeDto finishCommande(Long id) {
        Commande commande = this.commandeRepository.findById(id).orElseThrow(() -> new NotFound("Commande introuvable"));
        commande.setEnCours(false);
        this.commandeRepository.save(commande);
        return this.commandeMapper.toDto(commande);
    }

    private Boolean payCommande(Long id) {
        Commande commande = this.commandeRepository.findById(id).orElseThrow(() -> new NotFound("Commande introuvable"));
        commande.setIsPaye(true);
        this.commandeRepository.save(commande);
        return commande.getIsPaye();
    }

    public CommandeDto updateCommande(String compteJson, CommandeRequestDto commandeDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compte = objectMapper.readValue(compteJson, CompteDto.class);
            commandeDto.setCompteId(compte.getId());
            Optional<Commande> commande = this.commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(commandeDto.getCompteId());
            if(commande.isEmpty()){
                throw new NotFound("Commande introuvable");
            }
            return updatePanier(commandeDto, commande.get());
        }catch (JsonProcessingException e){
            throw new RuntimeException("Erreur lors de la mise à jour de la commande");
        }
    }

    private CommandeDto updatePanier(CommandeRequestDto commandeDto, Commande commande) {
        commande.getPanier().clear();
        for (PizzaPanierRequestDto pizzaPanierRequestDto : commandeDto.getPanier()) {
            PizzaPanier pizzaPanier1 = new PizzaPanier();
            Pizza pizza = pizzaRepository.findById(pizzaPanierRequestDto.getPizzaId()).orElseThrow();
            pizzaPanier1.setPizza(pizza);
            pizzaPanier1.setCommande(commande);
            List<Ingredient> ingredients = new ArrayList<>();
            for (Long ingredientId : pizzaPanierRequestDto.getIngredientsIds()) {
                Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow();
                ingredients.add(ingredient);
            }
            pizzaPanier1.setIngredients(ingredients);
            commande.getPanier().add(pizzaPanier1);
        }
        commande = this.commandeRepository.save(commande);
        return this.commandeMapper.toDto(commande);
    }

    public ResponseEntity<Map<String, String>> createCheckoutSession(String compteJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compte = objectMapper.readValue(compteJson, CompteDto.class);
            Optional<Commande> commande = this.commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compte.getId());
            if(commande.isEmpty()){
                throw new NotFound("Commande introuvable");
            }
            float total = 0.0f;
            for(PizzaPanier pizzaPanier: commande.get().getPanier()){
                for(Ingredient ingredient: pizzaPanier.getIngredients()){
                    total = total+ ingredient.getPrix();
                }
            }
            long montantEnCentimes = (long) (total * 100L);
            if(montantEnCentimes == 0){
                Map<String, String> response = new HashMap<>();
                response.put("url","http://localhost:5173/panier" );
                return ResponseEntity.ok(response);
            }
            Stripe.apiKey = "";

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
        } catch (JsonProcessingException | StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    public Boolean payerSuccess(String compteJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compte = objectMapper.readValue(compteJson, CompteDto.class);
            Optional<Commande> commande = this.commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compte.getId());
            if(commande.isEmpty()){
                throw new NotFound("Commande introuvable");
            }
            return payCommande(commande.get().getId());
        }catch (JsonProcessingException e){
            throw new NotFound("Erreur lors de la récupération de la commande");
        }
    }
}
