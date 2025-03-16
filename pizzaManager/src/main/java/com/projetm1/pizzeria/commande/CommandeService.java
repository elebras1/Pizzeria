package com.projetm1.pizzeria.commande;

import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.commande.dto.CommandeRequestDto;
import com.projetm1.pizzeria.compte.Compte;
import com.projetm1.pizzeria.compte.CompteMapper;
import com.projetm1.pizzeria.compte.CompteRepository;
import com.projetm1.pizzeria.ingredient.Ingredient;
import com.projetm1.pizzeria.ingredient.IngredientMapper;
import com.projetm1.pizzeria.ingredient.IngredientRepository;
import com.projetm1.pizzeria.pizza.Pizza;
import com.projetm1.pizzeria.pizza.PizzaRepository;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanier;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanierMapper;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanierRepository;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        commande.setIdCommentaires(commandeDto.getCommentairesIds());

        List<PizzaPanier> pizzaPanier = new ArrayList<>();
        commande.setPanier(pizzaPanier);

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
            pizzaPanier.add(pizzaPanier1);
        }
        commande = this.commandeRepository.save(commande);

        return this.commandeMapper.toDto(commande);
    }

    public void deleteCommandeById(Long id) {
        this.commandeRepository.deleteById(id);
    }
    
    public Boolean finishCommande(Long id) {
        Commande commande = this.commandeRepository.findById(id).orElseThrow();
        commande.setEnCours(false);
        this.commandeRepository.save(commande);
        return true;
    }

    public CommandeDto updateCommande(Long id, CommandeRequestDto commandeDto) {
        if(!this.commandeRepository.existsById(id)) {
            return null;
        }

        Commande commande = this.commandeMapper.toEntity(commandeDto);
        Compte compte = this.compteRepository.findById(commandeDto.getCompteId()).orElseThrow();
        commande.setCompte(compte);
        commande.setId(id);

        return this.commandeMapper.toDto(this.commandeRepository.save(commande));
    }

    public Long getLastCommandeIdByCompteId(Long id) {
        Commande commande=this.commandeRepository.findByEnCours(Boolean.TRUE);
        return commande.getId();
    }
}
