package com.projetm1.pizzeria.commande;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CommandeRepository extends JpaRepository<Commande, Long> {

    Commande findByEnCours(Boolean enCours);
    Commande findByCompteIdAndEnCoursTrue(Long compteId);
}
