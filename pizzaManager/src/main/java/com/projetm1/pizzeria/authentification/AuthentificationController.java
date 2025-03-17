package com.projetm1.pizzeria.authentification;

import com.projetm1.pizzeria.compte.dto.CompteDto;
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
}
