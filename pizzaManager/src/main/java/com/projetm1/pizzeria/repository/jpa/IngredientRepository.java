package com.projetm1.pizzeria.repository.jpa;

import com.projetm1.pizzeria.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
