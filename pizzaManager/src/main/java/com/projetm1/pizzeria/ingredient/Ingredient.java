package com.projetm1.pizzeria.ingredient;

import com.projetm1.pizzeria.pizza.Pizza;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanier;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;
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

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Ingredient that)) return false;

        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + this.id +
                ", nom='" + this.nom + '\'' +
                ", description='" + this.description + '\'' +
                ", prix=" + this.prix +
                '}';
    }
}
