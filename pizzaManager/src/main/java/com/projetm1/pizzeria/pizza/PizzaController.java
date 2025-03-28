package com.projetm1.pizzeria.pizza;

import com.projetm1.pizzeria.pizza.dto.PizzaDto;
import com.projetm1.pizzeria.pizza.dto.PizzaRequestDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<PizzaDto>> getAllPizzas() {
        return ResponseEntity.ok(this.pizzaService.getAllPizzas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PizzaDto> getPizzaById(@PathVariable Long id) {
        return ResponseEntity.ok(this.pizzaService.getPizzaById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PizzaDto> savePizza(@ModelAttribute PizzaRequestDto pizzaDto) {
        return ResponseEntity.ok(this.pizzaService.savePizza(pizzaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizzaById(@PathVariable Long id) {
        this.pizzaService.deletePizzaById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PizzaDto> updatePizza(@PathVariable Long id, @ModelAttribute PizzaRequestDto pizzaDto) {
        return ResponseEntity.ok(this.pizzaService.updatePizza(id, pizzaDto));
    }
}
