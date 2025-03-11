package com.projetm1.pizzeria.repository.jpa;

import com.projetm1.pizzeria.entities.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {

}
