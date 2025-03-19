package com.projetm1.pizzeria.ingredient;

import com.projetm1.pizzeria.error.Conflict;
import com.projetm1.pizzeria.error.NotFound;
import com.projetm1.pizzeria.ingredient.dto.IngredientDto;
import com.projetm1.pizzeria.ingredient.dto.IngredientRequestDto;
import org.springframework.dao.DataIntegrityViolationException;
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
        return this.ingredientMapper.toDto(this.ingredientRepository.findById(id).orElseThrow(() -> new NotFound("L'ingrédient n'existe pas")));
    }

    public List<IngredientDto> getAllIngredients() {
        List<IngredientDto> ingredientDtos = new ArrayList<>();
        for(Ingredient ingredient : this.ingredientRepository.findAll()) {
            ingredientDtos.add(this.ingredientMapper.toDto(ingredient));
        }

        return ingredientDtos;
    }

    public IngredientDto saveIngredient(IngredientRequestDto ingredientDto) {
        if(this.ingredientRepository.findByNom(ingredientDto.getNom()) != null) {
            throw new Conflict("L'ingrédient " + ingredientDto.getNom() + " existe déjà");
        }

        StringBuilder message = new StringBuilder();
        if(ingredientDto.getNom() == null || ingredientDto.getNom().isEmpty()) {
            message.append("Le nom de l'ingrédient est obligatoire\n");
        }

        if(ingredientDto.getDescription() == null || ingredientDto.getDescription().isEmpty()) {
            message.append("La description de l'ingrédient est obligatoire\n");
        }

        if(ingredientDto.getPrix() == null || ingredientDto.getPrix() <= 0) {
            message.append("Le prix de l'ingrédient est obligatoire et doit être supérieur à 0");
        }

        if(!message.isEmpty()) {
            throw new Conflict(message.toString());
        }

        return this.ingredientMapper.toDto(this.ingredientRepository.save(this.ingredientMapper.toEntity(ingredientDto)));
    }

    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
    public void deleteIngredientById(Long id) {
        if (!this.ingredientRepository.existsById(id)) {
            throw new Conflict("L'ingrédient n'existe pas");
        }

        try {
            this.ingredientRepository.deleteById(id);
            this.ingredientRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new Conflict("L'ingrédient est présent dans une ou plusieurs pizzas");
        }
    }

    public IngredientDto updateIngredient(Long id, IngredientRequestDto ingredientDto) {
        Ingredient ingredientOld = this.ingredientRepository.findById(id).orElse(null);
        if(ingredientOld == null) {
            throw new NotFound("L'ingrédient n'existe pas");
        }

        if(!ingredientOld.getNom().equals(ingredientDto.getNom()) && this.ingredientRepository.countByNom(ingredientDto.getNom()) > 0) {
            throw new Conflict("L'ingrédient " + ingredientDto.getNom() + " existe déjà");
        }

        StringBuilder message = new StringBuilder();
        if(ingredientDto.getNom() == null || ingredientDto.getNom().isEmpty()) {
            message.append("Le nom de l'ingrédient est obligatoire\n");
        }

        if(ingredientDto.getDescription() == null || ingredientDto.getDescription().isEmpty()) {
            message.append("La description de l'ingrédient est obligatoire\n");
        }

        if(ingredientDto.getPrix() == null || ingredientDto.getPrix() <= 0) {
            message.append("Le prix de l'ingrédient est obligatoire et doit être supérieur à 0");
        }

        if(!message.isEmpty()) {
            throw new Conflict(message.toString());
        }

        Ingredient ingredient = this.ingredientMapper.toEntity(ingredientDto);
        ingredient.setId(id);

        return this.ingredientMapper.toDto(this.ingredientRepository.save(ingredient));
    }
}
