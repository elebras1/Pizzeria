package com.projetm1.pizzeria.repository.mongo;

import com.projetm1.pizzeria.entities.Compte;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteRepository extends MongoRepository<Compte, String> {

}
