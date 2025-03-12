package com.projetm1.pizzeria.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "commande")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "commande")
    private Compte Compte;
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<Panier> paniers;
    private List<String> idCommentaires;

    public Commande() {
        this.paniers = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<Panier> getPaniers() {
        return paniers;
    }

    public void setPaniers(List<Panier> paniers) {
        this.paniers = paniers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Commande commande = (Commande) o;

        return Objects.equals(id, commande.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

