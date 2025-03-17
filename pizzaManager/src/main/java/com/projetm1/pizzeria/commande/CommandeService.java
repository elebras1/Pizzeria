package com.projetm1.pizzeria.commande;

import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.commande.dto.CommandeRequestDto;
import com.projetm1.pizzeria.compte.Compte;
import com.projetm1.pizzeria.compte.CompteRepository;
import com.projetm1.pizzeria.ingredient.Ingredient;
import com.projetm1.pizzeria.ingredient.IngredientRepository;
import com.projetm1.pizzeria.pizza.Pizza;
import com.projetm1.pizzeria.pizza.PizzaRepository;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanier;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        return this.commandeMapper.toDto(this.commandeRepository.findById(id).orElseThrow());
    }

    public List<CommandeDto> getAllCommandes() {
        return this.commandeRepository.findAll().stream().map(this.commandeMapper::toDto).collect(Collectors.toList());
    }

    public CommandeDto saveCommande(CommandeRequestDto commandeDto) {
        Commande commande = this.commandeMapper.toEntity(commandeDto);
        Compte compte = this.compteRepository.findById(commandeDto.getCompteId()).orElseThrow();
        commande.setCompte(compte);
        commande.setEnCours(true);
        commande.setIsPaye(false);
        commande.setDate(LocalDateTime.now());
        commande.setIdCommentaires(new ArrayList<>());
        return updatePanier(commandeDto, commande);
    }
    public Optional<Commande> getCommandeEnCoursByCompteId(Long compteId) {
        return this.commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compteId);

    }

    public void deleteCommandeById(Long id) {
        this.commandeRepository.deleteById(id);
    }
    
    public CommandeDto finishCommande(Long id) {
        Commande commande = this.commandeRepository.findById(id).orElseThrow();
        commande.setEnCours(false);
        this.commandeRepository.save(commande);
        return this.commandeMapper.toDto(commande);
    }
    public Boolean payCommande(Long id) {
        Commande commande = this.commandeRepository.findById(id).orElseThrow();
        commande.setIsPaye(true);
        this.commandeRepository.save(commande);
        return commande.getIsPaye();
    }

    public CommandeDto updateCommande(CommandeRequestDto commandeDto,Commande commande) {
        return updatePanier(commandeDto, commande);
    }

    private CommandeDto updatePanier(CommandeRequestDto commandeDto, Commande commande) {
        commande.getPanier().clear();

        System.out.println(commande.getPanier().toArray().length);

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
}
