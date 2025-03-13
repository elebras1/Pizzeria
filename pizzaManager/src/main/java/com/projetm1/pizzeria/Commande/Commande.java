package com.projetm1.pizzeria.Commande;

import com.projetm1.pizzeria.Compte.Compte;
import com.projetm1.pizzeria.PizzaPanier.PizzaPanier;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "commande")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_compte")
    private Compte compte;
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<PizzaPanier> panier;
    private List<String> idCommentaires;
}

