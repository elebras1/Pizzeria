package com.projetm1.pizzeria.pizza.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class PizzaRequestDto {
    private String nom;
    private String description;
    private MultipartFile photo;
    private Set<Long> standardIngredientsIds;
    private Set<Long> optionalIngredientsIds;
}
