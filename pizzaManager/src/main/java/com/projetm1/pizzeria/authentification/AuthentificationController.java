package com.projetm1.pizzeria.authentification;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentification")
public class AuthentificationController {
    private final AuthentificationService authentificationService;

    public AuthentificationController(AuthentificationService authentificationService) {
        this.authentificationService = authentificationService;
    }

    @PostMapping
    public Boolean verifyLogin(@RequestBody AuthentificationDto authentificationDto) {
        return this.authentificationService.verifyLogin(authentificationDto);
    }
}
