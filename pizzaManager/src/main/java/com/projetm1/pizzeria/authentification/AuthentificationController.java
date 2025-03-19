package com.projetm1.pizzeria.authentification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import com.projetm1.pizzeria.compte.dto.ComptePasswordChangeDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentification")
public class AuthentificationController {
    private final AuthentificationService authentificationService;

    public AuthentificationController(AuthentificationService authentificationService) {
        this.authentificationService = authentificationService;
    }

    @PostMapping
    public CompteDto verifyLogin(@RequestBody AuthentificationDto authentificationDto) {
        return this.authentificationService.verifyLogin(authentificationDto);
    }

    @GetMapping("/verifyAdmin")
    public CompteDto verifyAdmin(@RequestHeader("x-compte") String compteJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compteDto = objectMapper.readValue(compteJson, CompteDto.class);
            if(compteDto.getIsAdmin()) {
                return compteDto;
            } else {
                return null;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
