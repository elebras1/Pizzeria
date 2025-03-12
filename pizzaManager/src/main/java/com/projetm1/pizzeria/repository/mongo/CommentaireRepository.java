package com.projetm1.pizzeria.repository.mongo;

import com.projetm1.pizzeria.entities.Commentaire;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentaireRepository extends MongoRepository<Commentaire, String> {

}
