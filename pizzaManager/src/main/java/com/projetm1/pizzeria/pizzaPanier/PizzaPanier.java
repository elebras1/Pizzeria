package com.projetm1.pizzeria.pizzaPanier;

import com.projetm1.pizzeria.commande.Commande;
import com.projetm1.pizzeria.ingredient.Ingredient;
import com.projetm1.pizzeria.pizza.Pizza;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "pizza_panier")
public class PizzaPanier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_pizza", nullable = false)
    private Pizza pizza;
    @ManyToOne
    @JoinColumn(name = "id_commande", nullable = false)
    private Commande commande;
    @ManyToMany
    @JoinTable(
            name = "ingredients_panier",
            joinColumns = @JoinColumn(name = "pizza_panier_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;
}
