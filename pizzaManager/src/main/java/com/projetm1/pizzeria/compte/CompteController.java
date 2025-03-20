package com.projetm1.pizzeria.compte;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.compte.dto.ComptePasswordChangeDto;
import com.projetm1.pizzeria.compte.dto.CompteRequestDto;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/myaccount")
    public CompteDto getCompteByToken(@RequestHeader("x-compte") String compteJson) {
        return this.compteService.getCompteByToken(compteJson);
    }
    @PutMapping("/myaccount")
    public CompteDto updateCompteByToken(@RequestHeader("x-compte") String compteJson,@RequestBody CompteRequestDto compteRequestDto) {
        return this.compteService.updateCompteByToken(compteJson,compteRequestDto);
    }
    @PutMapping("/myaccount/password")
    public CompteDto updateComptePasswordByToken(@RequestHeader("x-compte") String compteJson,@RequestBody ComptePasswordChangeDto comptePasswordChangeDto) {
        return this.compteService.updateCompteByToken(compteJson,comptePasswordChangeDto);
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

    @GetMapping("/{id}/commandes")
    public List<CommandeDto> getCommandesByCompteId(@PathVariable Long id) {
        return this.compteService.getCommandesByCompteId(id);
    }

    @GetMapping("/commandes")
    public List<CommandeDto> getCommandesByCompteId(@RequestHeader("x-compte") String compteJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CompteDto compteDto = objectMapper.readValue(compteJson, CompteDto.class);
        return this.compteService.getCommandesByCompteId(compteDto.getId());
    }
}
