package com.projetm1.pizzeria.entities;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    private int prix;
    @ManyToMany(mappedBy = "standardIngredients")
    private Set<Pizza> pizzasStandard = new HashSet<>();
    @ManyToMany(mappedBy = "optionalIngredients")
    private Set<Pizza> pizzasOptional = new HashSet<>();
    @ManyToMany(mappedBy = "ingredients")
    private Set<PizzaPanier> pizzaPaniers;

    public Ingredient() {
        this.pizzasStandard = new HashSet<>();
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

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public Set<Pizza> getPizzasStandard() {
        return pizzasStandard;
    }

    public void setPizzasStandard(Set<Pizza> pizzasStandard) {
        this.pizzasStandard = pizzasStandard;
    }

    public Set<Pizza> getPizzasOptional() {
        return pizzasOptional;
    }

    public void setPizzasOptional(Set<Pizza> pizzasOptional) {
        this.pizzasOptional = pizzasOptional;
    }
}
