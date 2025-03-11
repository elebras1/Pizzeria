package com.projetm1.pizzeria.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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

    public Pizza() {
        this.standardIngredients = new HashSet<>();
        this.optionalIngredients = new HashSet<>();
        this.pizzaPaniers = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Set<Ingredient> getStandardIngredients() {
        return standardIngredients;
    }

    public void setStandardIngredients(Set<Ingredient> standardIngredients) {
        this.standardIngredients = standardIngredients;
    }

    public Set<Ingredient> getOptionalIngredients() {
        return optionalIngredients;
    }

    public void setOptionalIngredients(Set<Ingredient> optionalIngredients) {
        this.optionalIngredients = optionalIngredients;
    }

    public Set<PizzaPanier> getPizzaPaniers() {
        return pizzaPaniers;
    }

    public void setPizzaPaniers(Set<PizzaPanier> pizzaPaniers) {
        this.pizzaPaniers = pizzaPaniers;
    }
}
