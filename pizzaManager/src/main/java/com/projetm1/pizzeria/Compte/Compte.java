package com.projetm1.pizzeria.Compte;

import com.projetm1.pizzeria.Commande.Commande;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "compte")
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    private String pseudo;
    private String prenom;
    private String nom;
    private String motDePasse;
    private Boolean isAdmin;
    @OneToMany(mappedBy = "compte")
    private List<Commande> commandes;
}

