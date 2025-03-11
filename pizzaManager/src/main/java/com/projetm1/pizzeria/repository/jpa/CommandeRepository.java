package com.projetm1.pizzeria.repository.jpa;

import com.projetm1.pizzeria.entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

}
