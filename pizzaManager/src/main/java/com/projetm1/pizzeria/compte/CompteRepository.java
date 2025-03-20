package com.projetm1.pizzeria.compte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

    boolean existsByPseudo(String pseudo);
}
