package com.projetm1.pizzeria.commande;

import com.projetm1.pizzeria.compte.Compte;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanier;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
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
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PizzaPanier> panier;
    private Boolean enCours;
    private Boolean isPaye;
    private LocalDateTime date;
    @ElementCollection
    @CollectionTable(name = "commentaires", joinColumns = @JoinColumn(name = "commande_id"))
    @Column(name = "commentaire")
    private List<String> idCommentaires;

}

