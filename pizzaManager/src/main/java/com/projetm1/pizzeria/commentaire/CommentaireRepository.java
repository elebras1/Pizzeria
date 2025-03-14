package com.projetm1.pizzeria.commentaire;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentaireRepository extends MongoRepository<Commentaire, String> {

}
