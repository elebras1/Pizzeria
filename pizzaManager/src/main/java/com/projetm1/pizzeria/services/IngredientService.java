package com.projetm1.pizzeria.services;

import com.projetm1.pizzeria.dtos.IngredientDto;
import com.projetm1.pizzeria.mappers.IngredientMapper;
import com.projetm1.pizzeria.repository.jpa.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ingredientService")
@Transactional
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    private IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
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
