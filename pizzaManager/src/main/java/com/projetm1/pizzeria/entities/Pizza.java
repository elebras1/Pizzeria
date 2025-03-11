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
    @OneToMany(mappedBy = "pizza", cascade = CascadeType.ALL)
    private Set<PizzaPanier> pizzaPaniers;

    public Pizza() {
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
    public Set<PizzaPanier> getPizzaPaniers() {
        return pizzaPaniers;
    }
    public void setPizzaPaniers(Set<PizzaPanier> pizzaPaniers) {
        this.pizzaPaniers = pizzaPaniers;
    }
}
