package com.projetm1.pizzeria.pizzaPanier;

import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierDto;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierRequestDto;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<PizzaPanierDto>> getAllPizzasPaniers() {
        return ResponseEntity.ok(this.pizzaPanierService.getAllPizzasPaniers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PizzaPanierDto> getPizzaPanierById(@PathVariable Long id) {
        return ResponseEntity.ok(this.pizzaPanierService.getPizzaPanierById(id));
    }

    @PostMapping
    public ResponseEntity<PizzaPanierDto> savePizzaPanier(@RequestBody PizzaPanierRequestDto pizzaPanierDto) {
        return ResponseEntity.ok(this.pizzaPanierService.savePizzaPanier(pizzaPanierDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizzaPanierById(@PathVariable Long id) {
        this.pizzaPanierService.deletePizzaPanierById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PizzaPanierDto> updatePizzaPanier(@PathVariable Long id, @RequestBody PizzaPanierRequestDto pizzaPanierDto) {
        return ResponseEntity.ok(this.pizzaPanierService.updatePizzaPanier(id, pizzaPanierDto));
    }
}
