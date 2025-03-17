package com.projetm1.pizzeria.commande;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CommandeRepository extends JpaRepository<Commande, Long> {

    Commande findByEnCours(Boolean enCours);
    Commande findByCompteIdAndEnCoursTrue(Long compteId);
    Optional<Commande> findByCompteIdAndEnCoursTrueAndIsPayeFalse(Long compteId);
}
