package com.projetm1.pizzeria.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    @JoinColumn(name = "id_panier", nullable = false)
    private Panier panier;
    @ManyToMany
    @JoinTable(
            name = "ingredients_panier",
            joinColumns = @JoinColumn(name = "pizza_panier_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> ingredients;

    public PizzaPanier() {
        this.ingredients = new HashSet<>();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Pizza getPizza() {
        return pizza;
    }
    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }
    public Panier getPanier() {
        return panier;
    }
    public void setPanier(Panier panier) {
        this.panier = panier;
    }
    public Set<Ingredient> getIngredients() {
        return ingredients;
    }
    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PizzaPanier that = (PizzaPanier) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
