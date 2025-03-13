package com.projetm1.pizzeria.ingredient;

import com.projetm1.pizzeria.ingredient.dto.IngredientDto;
import com.projetm1.pizzeria.ingredient.dto.IngredientRequestDto;
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
    public List<IngredientDto> getAllIngredients() {
        return this.ingredientService.getAllIngredients();
    }

    @GetMapping("/{id}")
    public IngredientDto getIngredientById(@PathVariable Long id) {
        return this.ingredientService.getIngredientById(id);
    }

    @PostMapping
    public IngredientDto saveIngredient(@RequestBody IngredientRequestDto ingredientDto) {
        return this.ingredientService.saveIngredient(ingredientDto);
    }

    @DeleteMapping("/{id}")
    public void deleteIngredientById(@PathVariable Long id) {
        this.ingredientService.deleteIngredientById(id);
    }

    @PutMapping("/{id}")
    public IngredientDto updateIngredient(@PathVariable Long id, @RequestBody IngredientRequestDto ingredientDto) {
        return this.ingredientService.updateIngredient(id, ingredientDto);
    }
}
