package com.projetm1.pizzeria.authentification;

import com.projetm1.pizzeria.compte.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthentificationRepository extends JpaRepository<Compte, Long> {

    Compte findByPseudo(String username);
}