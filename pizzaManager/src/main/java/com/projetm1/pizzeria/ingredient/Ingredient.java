package com.projetm1.pizzeria.ingredient;

import com.projetm1.pizzeria.pizza.Pizza;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanier;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    private Float prix;
    @ManyToMany(mappedBy = "standardIngredients")
    private Set<Pizza> pizzasStandard;
    @ManyToMany(mappedBy = "optionalIngredients")
    private Set<Pizza> pizzasOptionnel;
    @ManyToMany(mappedBy = "ingredients")
    private Set<PizzaPanier> pizzaPaniers;
}
