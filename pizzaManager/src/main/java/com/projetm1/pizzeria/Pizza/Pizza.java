package com.projetm1.pizzeria.Pizza;

import com.projetm1.pizzeria.Ingredient.Ingredient;
import com.projetm1.pizzeria.PizzaPanier.PizzaPanier;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "pizza")
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    private String photo;
    @ManyToMany
    @JoinTable(
            name = "ingredient_standard",
            joinColumns = @JoinColumn(name = "id_pizza"),
            inverseJoinColumns = @JoinColumn(name = "id_ingredient")
    )
    private Set<Ingredient> standardIngredients;
    @ManyToMany
    @JoinTable(
            name = "ingredient_optionnel",
            joinColumns = @JoinColumn(name = "id_pizza"),
            inverseJoinColumns = @JoinColumn(name = "id_ingredient")
    )
    private Set<Ingredient> optionalIngredients;
    @OneToMany(mappedBy = "pizza", cascade = CascadeType.ALL)
    private Set<PizzaPanier> pizzaPaniers;
}
