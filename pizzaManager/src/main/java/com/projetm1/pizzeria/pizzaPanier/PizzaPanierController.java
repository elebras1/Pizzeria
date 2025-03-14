package com.projetm1.pizzeria.pizzaPanier;

import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierDto;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierRequestDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas-paniers")
public class PizzaPanierController {
    private final PizzaPanierService pizzaPanierService;

    public PizzaPanierController(PizzaPanierService pizzaPanierService) {
        this.pizzaPanierService = pizzaPanierService;
    }

    @GetMapping
    public List<PizzaPanierDto> getAllPizzasPaniers() {
        return this.pizzaPanierService.getAllPizzasPaniers();
    }

    @GetMapping("/{id}")
    public PizzaPanierDto getPizzaPanierById(@PathVariable Long id) {
        return this.pizzaPanierService.getPizzaPanierById(id);
    }

    @PostMapping
    public PizzaPanierDto savePizzaPanier(@RequestBody PizzaPanierRequestDto pizzaPanierDto) {
        return this.pizzaPanierService.savePizzaPanier(pizzaPanierDto);
    }

    @DeleteMapping("/{id}")
    public void deletePizzaPanierById(@PathVariable Long id) {
        this.pizzaPanierService.deletePizzaPanierById(id);
    }

    @PutMapping("/{id}")
    public PizzaPanierDto updatePizzaPanier(@PathVariable Long id, @RequestBody PizzaPanierRequestDto pizzaPanierDto) {
        return this.pizzaPanierService.updatePizzaPanier(id, pizzaPanierDto);
    }
}
