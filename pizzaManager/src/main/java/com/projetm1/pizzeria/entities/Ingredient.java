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
    @ManyToMany(mappedBy = "ingredient_standard")
    private Set<Pizza> pizzasStandard;
    @ManyToMany(mappedBy = "ingredient_optionnel")
    private Set<Pizza> pizzasOptionnel;
    @ManyToMany(mappedBy = "ingredients_panier")
    private Set<PizzaPanier> pizzaPaniers;

    public Ingredient() {
        this.pizzaPaniers = new HashSet<>();
        this.pizzasOptionnel = new HashSet<>();
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

    private Set<PizzaPanier> getPizzaPaniers() {
        return pizzaPaniers;
    }

    private void setPizzaPaniers(Set<PizzaPanier> pizzaPaniers) {
        this.pizzaPaniers = pizzaPaniers;
    }

    public Set<Pizza> getPizzasStandard() {
        return pizzasStandard;
    }

    public void setPizzasStandard(Set<Pizza> pizzasStandard) {
        this.pizzasStandard = pizzasStandard;
    }

    public Set<Pizza> getPizzasOptionnel() {
        return pizzasOptionnel;
    }

    public void setPizzasOptionnel(Set<Pizza> pizzasOptionnel) {
        this.pizzasOptionnel = pizzasOptionnel;
    }
}
