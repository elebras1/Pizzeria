package com.projetm1.pizzeria.commande;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

}
