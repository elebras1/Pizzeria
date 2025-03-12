package com.projetm1.pizzeria.repository.jpa;

import com.projetm1.pizzeria.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

}
