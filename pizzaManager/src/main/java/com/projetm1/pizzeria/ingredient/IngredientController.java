package com.projetm1.pizzeria.ingredient;

import com.projetm1.pizzeria.ingredient.dto.IngredientDto;
import com.projetm1.pizzeria.ingredient.dto.IngredientRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {
        return ResponseEntity.ok(this.ingredientService.getAllIngredients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredientById(@PathVariable Long id) {
        return ResponseEntity.ok(this.ingredientService.getIngredientById(id));
    }

    @PostMapping
    public ResponseEntity<IngredientDto> saveIngredient(@RequestBody IngredientRequestDto ingredientDto) {
        return ResponseEntity.ok(this.ingredientService.saveIngredient(ingredientDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredientById(@PathVariable Long id) {
        this.ingredientService.deleteIngredientById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDto> updateIngredient(@PathVariable Long id, @RequestBody IngredientRequestDto ingredientDto) {
        return ResponseEntity.ok(this.ingredientService.updateIngredient(id, ingredientDto));
    }
}
