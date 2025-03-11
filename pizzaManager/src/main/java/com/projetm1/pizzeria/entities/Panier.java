package com.projetm1.pizzeria.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "panier")
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_commande")
    private Commande commande;
    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL)
    private Set<PizzaPanier> pizzaPaniers;

    public Panier() {
        this.pizzaPaniers = new HashSet<>();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Commande getCommande() {
        return commande;
    }
    public void setCommande(Commande commande) {
        this.commande = commande;
    }
    public Set<PizzaPanier> getPizzaPaniers() {
        return pizzaPaniers;
    }
    public void setPizzaPaniers(Set<PizzaPanier> pizzaPaniers) {
        this.pizzaPaniers = pizzaPaniers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Panier panier = (Panier) o;

        return Objects.equals(id, panier.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

