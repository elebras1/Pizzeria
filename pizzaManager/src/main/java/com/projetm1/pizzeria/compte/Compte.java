package com.projetm1.pizzeria.compte;

import com.projetm1.pizzeria.commande.Commande;
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
    @Column(unique = true)
    private String pseudo;
    private String prenom;
    private String nom;
    private String motDePasse;
    private Boolean isAdmin;
    @OneToMany(mappedBy = "compte")
    private List<Commande> commandes;
}

