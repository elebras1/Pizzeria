package com.projetm1.pizzeria.repository.jpa;

import com.projetm1.pizzeria.entities.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {

}
