package com.projetm1.pizzeria.pizza;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    @Query("SELECT p FROM Pizza p LEFT JOIN FETCH p.standardIngredients LEFT JOIN FETCH p.optionalIngredients WHERE p.id = :id")
    Optional<Pizza> findByIdWithIngredients(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Pizza p LEFT JOIN FETCH p.standardIngredients LEFT JOIN FETCH p.optionalIngredients")
    List<Pizza> findAllWithIngredients();
}
