package com.projetm1.pizzeria.pizza;

import com.projetm1.pizzeria.pizza.dto.PizzaDto;
import com.projetm1.pizzeria.pizza.dto.PizzaRequestDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {
    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public List<PizzaDto> getAllPizzas() {
        return this.pizzaService.getAllPizzas();
    }

    @GetMapping("/{id}")
    public PizzaDto getPizzaById(@PathVariable Long id) {
        return this.pizzaService.getPizzaById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PizzaDto savePizza(@ModelAttribute PizzaRequestDto pizzaDto) {
        return this.pizzaService.savePizza(pizzaDto);
    }

    @DeleteMapping("/{id}")
    public void deletePizzaById(@PathVariable Long id) {
        this.pizzaService.deletePizzaById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PizzaDto updatePizza(@PathVariable Long id, @ModelAttribute PizzaRequestDto pizzaDto) {
        return this.pizzaService.updatePizza(id, pizzaDto);
    }
}
