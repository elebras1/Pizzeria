package com.projetm1.pizzeria.Compte;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compte")
public class CompteController {
    private final CompteService compteService;
    private final CompteMapper compteMapper;

    public CompteController(CompteService compteService,CompteMapper compteMapper) {
        this.compteService = compteService;
        this.compteMapper = compteMapper;
    }

    @GetMapping
    public List<CompteDto> getAllCompte() {
        return this.compteService.getAllCompte();
    }

    @GetMapping("/{id}")
    public CompteDto getCompteById(Long id) {
        return this.compteService.getCompteById(id);
    }

    @PostMapping
    public CompteDto addCompte(@RequestBody CompteDto compteDto) {
        return this.compteService.saveCompte(compteDto);
    }

    @PutMapping
    public CompteDto updateCompte(@RequestBody CompteDto compteDto) {
        return this.compteService.updateCompte(compteDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCompteById(@PathVariable Long id) {
        this.compteService.deleteCompteById(id);
    }

}
