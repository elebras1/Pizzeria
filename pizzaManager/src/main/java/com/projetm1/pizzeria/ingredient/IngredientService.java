package com.projetm1.pizzeria.ingredient;

import com.projetm1.pizzeria.ingredient.dto.IngredientDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ingredientService")
@Transactional
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    public IngredientDto getIngredientById(Long id) {
        return this.ingredientMapper.toDto(this.ingredientRepository.findById(id).orElse(null));
    }

    public IngredientDto saveIngredient(IngredientDto ingredientDto) {
        return this.ingredientMapper.toDto(this.ingredientRepository.save(this.ingredientMapper.toEntity(ingredientDto)));
    }

    public void deleteIngredientById(Long id) {
        this.ingredientRepository.deleteById(id);
    }

    public IngredientDto updateIngredient(IngredientDto ingredientDto) {
        return this.ingredientMapper.toDto(this.ingredientRepository.save(this.ingredientMapper.toEntity(ingredientDto)));
    }
}
