package com.projetm1.pizzeria.entities;

import jakarta.persistence.*;
import java.util.HashSet;
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
}

