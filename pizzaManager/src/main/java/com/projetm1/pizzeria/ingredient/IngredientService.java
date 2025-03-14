package com.projetm1.pizzeria.ingredient;

import com.projetm1.pizzeria.ingredient.dto.IngredientDto;
import com.projetm1.pizzeria.ingredient.dto.IngredientRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    public List<IngredientDto> getAllIngredients() {
        List<IngredientDto> ingredientDtos = new ArrayList<>();
        for(Ingredient ingredient : this.ingredientRepository.findAll()) {
            ingredientDtos.add(this.ingredientMapper.toDto(ingredient));
        }

        return ingredientDtos;
    }

    public IngredientDto saveIngredient(IngredientRequestDto ingredientDto) {
        return this.ingredientMapper.toDto(this.ingredientRepository.save(this.ingredientMapper.toEntity(ingredientDto)));
    }

    public void deleteIngredientById(Long id) {
        this.ingredientRepository.deleteById(id);
    }

    public IngredientDto updateIngredient(Long id, IngredientRequestDto ingredientDto) {
        if(!this.ingredientRepository.existsById(id)) {
            return null;
        }

        Ingredient ingredient = this.ingredientMapper.toEntity(ingredientDto);
        ingredient.setId(id);

        return this.ingredientMapper.toDto(this.ingredientRepository.save(ingredient));
    }
}
