package com.projetm1.pizzeria.services;

import com.projetm1.pizzeria.dtos.PizzaPanierDto;
import com.projetm1.pizzeria.entities.PizzaPanier;
import com.projetm1.pizzeria.mappers.PizzaPanierMapper;
import com.projetm1.pizzeria.repository.jpa.PizzaPanierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("pizzaPanierService")
@Transactional
public class PizzaPanierService {
    private final PizzaPanierRepository pizzaPanierRepository;
    private final PizzaPanierMapper pizzaPanierMapper;

    public PizzaPanierService(PizzaPanierRepository pizzaPanierRepository, PizzaPanierMapper pizzaPanierMapper) {
        this.pizzaPanierRepository = pizzaPanierRepository;
        this.pizzaPanierMapper = pizzaPanierMapper;
    }

    public PizzaPanierDto getPizzaById(Long id) {
        return this.pizzaPanierMapper.toDto(this.pizzaPanierRepository.findById(id).orElse(null));
    }

    public List<PizzaPanierDto> getAllPizza() {
        List<PizzaPanierDto> pizzaDtos = new ArrayList<>();
        for(PizzaPanier pizzaPanier : this.pizzaPanierRepository.findAll()) {
            pizzaDtos.add(this.pizzaPanierMapper.toDto(pizzaPanier));
        }

        return pizzaDtos;
    }

    public PizzaPanierDto savePizza(PizzaPanierDto pizzaDto) {
        PizzaPanier pizza = this.pizzaPanierMapper.toEntity(pizzaDto);
        pizza = this.pizzaPanierRepository.save(pizza);

        return this.pizzaPanierMapper.toDto(pizza);
    }

    public void deletePizzaById(Long id) {
        this.pizzaPanierRepository.deleteById(id);
    }
}
