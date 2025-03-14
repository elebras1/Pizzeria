package com.projetm1.pizzeria.compte;

import com.projetm1.pizzeria.compte.dto.CompteDto;
import com.projetm1.pizzeria.compte.dto.CompteRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("compteService")
@Transactional
public class CompteService {
    private final CompteRepository compteRepository;
    private final CompteMapper compteMapper;

    public CompteService(CompteRepository compteRepository, CompteMapper compteMapper) {
        this.compteRepository = compteRepository;
        this.compteMapper = compteMapper;
    }

    public CompteDto getCompteById(Long id) {
        return compteMapper.toDto(this.compteRepository.findById(id).orElse(null));
    }

    public List<CompteDto> getAllCompte() {
        List<CompteDto> compteDtos = new ArrayList<>();
        for(Compte compte : this.compteRepository.findAll()) {
            compteDtos.add(this.compteMapper.toDto(compte));
        }

        return compteDtos;
    }

    public CompteDto saveCompte(CompteRequestDto compteDto) {
        Compte compte = this.compteMapper.toEntity(compteDto);
        compte = this.compteRepository.save(compte);

        return this.compteMapper.toDto(compte);
    }

    public void deleteCompteById(Long id) {
        this.compteRepository.deleteById(id);
    }

    public CompteDto updateCompte(CompteRequestDto compteDto) {
        Compte compte = this.compteMapper.toEntity(compteDto);
        compte = this.compteRepository.save(compte);

        return this.compteMapper.toDto(compte);
    }
}
