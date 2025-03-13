package com.projetm1.pizzeria.pizza;

import com.projetm1.pizzeria.pizza.dto.PizzaDto;
import com.projetm1.pizzeria.pizza.dto.PizzaRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("pizzaService")
@Transactional
public class PizzaService {
    private final PizzaRepository pizzaRepository;
    private final PizzaMapper pizzaMapper;

    public PizzaService(PizzaRepository pizzaRepository, PizzaMapper pizzaMapper) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaMapper = pizzaMapper;
    }

    public PizzaDto getPizzaById(Long id) {
        return this.pizzaMapper.toDto(this.pizzaRepository.findById(id).orElse(null));
    }

    public List<PizzaDto> getAllPizzas() {
        List<PizzaDto> pizzaDtos = new ArrayList<>();
        for(Pizza pizza : this.pizzaRepository.findAll()) {
            pizzaDtos.add(this.pizzaMapper.toDto(pizza));
        }

        return pizzaDtos;
    }

    public PizzaDto savePizza(PizzaRequestDto pizzaDto) {
        Pizza pizza = this.pizzaMapper.toEntity(pizzaDto);
        pizza = this.pizzaRepository.save(pizza);

        return this.pizzaMapper.toDto(pizza);
    }

    public void deletePizzaById(Long id) {
        this.pizzaRepository.deleteById(id);
    }

    public PizzaDto updatePizza(Long id, PizzaRequestDto pizzaDto) {
        if(!this.pizzaRepository.existsById(id)) {
            return null;
        }

        Pizza pizza = this.pizzaMapper.toEntity(pizzaDto);
        pizza.setId(id);

        return this.pizzaMapper.toDto(this.pizzaRepository.save(pizza));
    }
}
