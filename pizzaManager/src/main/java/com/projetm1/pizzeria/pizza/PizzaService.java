package com.projetm1.pizzeria.pizza;

import com.projetm1.pizzeria.error.NotFound;
import com.projetm1.pizzeria.error.UnprocessableEntity;
import com.projetm1.pizzeria.image.ImageService;
import com.projetm1.pizzeria.pizza.dto.PizzaDto;
import com.projetm1.pizzeria.pizza.dto.PizzaRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service("pizzaService")
@Transactional
public class PizzaService {
    private final PizzaRepository pizzaRepository;
    private final PizzaMapper pizzaMapper;
    private final ImageService imageService;

    public PizzaService(PizzaRepository pizzaRepository, PizzaMapper pizzaMapper, ImageService imageService) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaMapper = pizzaMapper;
        this.imageService = imageService;
    }

    public PizzaDto getPizzaById(Long id) {
        if(!this.pizzaRepository.existsById(id)) {
            throw new NotFound("La pizza n'existe pas");
        }

        return this.pizzaMapper.toDto(this.pizzaRepository.findByIdWithIngredients(id).orElseThrow(() -> new NotFound("La pizza n'existe pas")));
    }

    public List<PizzaDto> getAllPizzas() {
        List<PizzaDto> pizzaDtos = new ArrayList<>();
        for(Pizza pizza : this.pizzaRepository.findAllWithIngredients()) {
            pizzaDtos.add(this.pizzaMapper.toDto(pizza));
        }

        return pizzaDtos;
    }

    public PizzaDto savePizza(PizzaRequestDto pizzaDto) {
        String fileName = this.imageService.saveImage(pizzaDto.getPhoto());

        StringBuilder message = new StringBuilder();
        if(pizzaDto.getNom() == null || pizzaDto.getNom().isEmpty()) {
            message.append("Le nom de la pizza est obligatoire\n");
        }

        if(pizzaDto.getDescription() == null || pizzaDto.getDescription().isEmpty()) {
            message.append("La description de la pizza est obligatoire\n");
        }

        if(pizzaDto.getStandardIngredientsIds() == null || pizzaDto.getOptionalIngredientsIds().isEmpty()) {
            message.append("Les ingrédients de base de la pizza sont obligatoires\n");
        }

        if(pizzaDto.getOptionalIngredientsIds() == null || pizzaDto.getOptionalIngredientsIds().isEmpty()) {
            message.append("Les ingrédients optionnels de la pizza sont obligatoires\n");
        }

        if(fileName == null) {
            message.append("La photo de la pizza est obligatoire");
        }

        if(!message.isEmpty()) {
            throw new UnprocessableEntity(message.toString());
        }

        Pizza pizza = this.pizzaMapper.toEntity(pizzaDto);
        pizza.setPhoto(fileName);
        pizza = this.pizzaRepository.save(pizza);

        return this.pizzaMapper.toDto(pizza);
    }

    public void deletePizzaById(Long id) {
        if(!this.pizzaRepository.existsById(id)) {
            throw new NotFound("La pizza n'existe pas");
        }

        this.pizzaRepository.deleteById(id);
    }

    public PizzaDto updatePizza(Long id, PizzaRequestDto pizzaDto) {
        Pizza pizzaOld = this.pizzaRepository.findById(id).orElse(null);
        if(pizzaOld == null) {
            return null;
        }
        String fileName = this.imageService.saveImage(pizzaDto.getPhoto());

        StringBuilder message = new StringBuilder();
        if(pizzaDto.getNom() == null || pizzaDto.getNom().isEmpty()) {
            message.append("Le nom de la pizza est obligatoire\n");
        }

        if(pizzaDto.getDescription() == null || pizzaDto.getDescription().isEmpty()) {
            message.append("La description de la pizza est obligatoire\n");
        }

        if(pizzaDto.getStandardIngredientsIds() == null || pizzaDto.getOptionalIngredientsIds().isEmpty()) {
            message.append("Les ingrédients de base de la pizza sont obligatoires\n");
        }

        if(pizzaDto.getOptionalIngredientsIds() == null || pizzaDto.getOptionalIngredientsIds().isEmpty()) {
            message.append("Les ingrédients optionnels de la pizza sont obligatoires\n");
        }

        if(!message.isEmpty()) {
            throw new UnprocessableEntity(message.toString());
        }

        Pizza pizza = this.pizzaMapper.toEntity(pizzaDto);
        pizza.setId(id);
        if(fileName != null) {
            pizza.setPhoto(fileName);
        } else {
            pizza.setPhoto(pizzaOld.getPhoto());
        }

        return this.pizzaMapper.toDto(this.pizzaRepository.save(pizza));
    }
}
