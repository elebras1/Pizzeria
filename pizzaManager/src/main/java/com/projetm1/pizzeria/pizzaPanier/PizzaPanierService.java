package com.projetm1.pizzeria.pizzaPanier;

import com.projetm1.pizzeria.commande.Commande;
import com.projetm1.pizzeria.commande.CommandeRepository;
import com.projetm1.pizzeria.ingredient.Ingredient;
import com.projetm1.pizzeria.ingredient.IngredientRepository;
import com.projetm1.pizzeria.pizza.Pizza;
import com.projetm1.pizzeria.pizza.PizzaRepository;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierDto;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("pizzaPanierService")
@Transactional
public class PizzaPanierService {
    private final PizzaPanierRepository pizzaPanierRepository;
    private final PizzaPanierMapper pizzaPanierMapper;
    private final PizzaRepository pizzaRepository;
    private final IngredientRepository ingredientRepository;

    public PizzaPanierService(PizzaPanierRepository pizzaPanierRepository, PizzaPanierMapper pizzaPanierMapper, PizzaRepository pizzaRepository, IngredientRepository ingredientRepository) {
        this.pizzaPanierRepository = pizzaPanierRepository;
        this.pizzaPanierMapper = pizzaPanierMapper;
        this.pizzaRepository = pizzaRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public PizzaPanierDto getPizzaPanierById(Long id) {
        return this.pizzaPanierMapper.toDto(this.pizzaPanierRepository.findById(id).orElse(null));
    }

    public List<PizzaPanierDto> getAllPizzaPanier() {
        List<PizzaPanierDto> pizzaPanierDtos = new ArrayList<>();
        for(PizzaPanier pizzaPanier : this.pizzaPanierRepository.findAll()) {
            pizzaPanierDtos.add(this.pizzaPanierMapper.toDto(pizzaPanier));
        }

        return pizzaPanierDtos;
    }

    public PizzaPanierDto savePizzaPanier(PizzaPanierRequestDto pizzaPanierDto) {
        PizzaPanier pizzaPanier = this.pizzaPanierMapper.toEntity(pizzaPanierDto);
        Pizza pizza = this.pizzaRepository.findById(pizzaPanierDto.getPizzaId()).orElse(null);
        Set<Ingredient> ingredients = new HashSet<>();
        for(Long ingredientId : pizzaPanierDto.getIngredientsIds()) {
            this.ingredientRepository.findById(ingredientId).ifPresent(ingredients::add);
        }
        pizzaPanier.setPizza(pizza);
        pizzaPanier.setIngredients(ingredients);

        pizzaPanier = this.pizzaPanierRepository.save(pizzaPanier);

        return this.pizzaPanierMapper.toDto(pizzaPanier);
    }

    public void deletePizzaPanierById(Long id) {
        this.pizzaPanierRepository.deleteById(id);
    }

    public PizzaPanierDto updatePizzaPanier(Long id, PizzaPanierRequestDto pizzaPanierDto) {
        if(!this.pizzaPanierRepository.existsById(id)) {
            return null;
        }

        PizzaPanier pizzaPanier = this.pizzaPanierMapper.toEntity(pizzaPanierDto);
        pizzaPanier.setId(id);
        Pizza pizza = this.pizzaRepository.findById(pizzaPanierDto.getPizzaId()).orElse(null);
        Set<Ingredient> ingredients = new HashSet<>();
        for(Long ingredientId : pizzaPanierDto.getIngredientsIds()) {
            this.ingredientRepository.findById(ingredientId).ifPresent(ingredients::add);
        }
        pizzaPanier.setPizza(pizza);
        pizzaPanier.setIngredients(ingredients);
        pizzaPanier = this.pizzaPanierRepository.save(pizzaPanier);
        return this.pizzaPanierMapper.toDto(pizzaPanier);
    }
}
