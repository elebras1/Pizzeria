package com.projetm1.pizzeria.compte;

import com.projetm1.pizzeria.compte.dto.CompteRequestDto;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {
    private final CompteService compteService;

    public CompteController(CompteService compteService) {
        this.compteService = compteService;
    }

    @GetMapping
    public List<CompteDto> getAllCompte() {
        return this.compteService.getAllCompte();
    }

    @GetMapping("/{id}")
    public CompteDto getCompteById(@PathVariable String id) {
        return this.compteService.getCompteById(Long.parseLong(id));
    }

    @PostMapping
    public CompteDto saveCompte(@RequestBody CompteRequestDto compteDto) {
        return this.compteService.saveCompte(compteDto);
    }

    @PutMapping("/{id}")
    public CompteDto updateCompte(@PathVariable Long id, @RequestBody CompteRequestDto compteDto) {
        return this.compteService.updateCompte(id, compteDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCompteById(@PathVariable Long id) {
        this.compteService.deleteCompteById(id);
    }

}
