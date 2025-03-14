package com.projetm1.pizzeria.pizza;

import com.projetm1.pizzeria.ingredient.Ingredient;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanier;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;
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

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Pizza pizza)) return false;

        return Objects.equals(this.id, pizza.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + this.id +
                ", nom='" + this.nom + '\'' +
                ", description='" + this.description + '\'' +
                ", photo='" + this.photo + '\'' +
                ", standardIngredients=" + this.standardIngredients +
                ", optionalIngredients=" + this.optionalIngredients +
                '}';
    }
}
