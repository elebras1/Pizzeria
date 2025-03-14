package com.projetm1.pizzeria.compte;

import com.projetm1.pizzeria.compte.dto.CompteRequestDto;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;

@Service("compteService")
@Transactional
public class CompteService {
    private final CompteRepository compteRepository;
    private final CompteMapper compteMapper;
    private final PasswordEncoder passwordEncoder;

    public CompteService(CompteRepository compteRepository, CompteMapper compteMapper, PasswordEncoder passwordEncoder) {
        this.compteRepository = compteRepository;
        this.compteMapper = compteMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public CompteDto getCompteById(Long id) {
        return this.compteMapper.toDto(this.compteRepository.findById(id).orElse(null));
    }

    public List<CompteDto> getAllCompte() {
        List<CompteDto> CompteNoPasswordDtos = new ArrayList<>();
        for(Compte compte : this.compteRepository.findAll()) {
            CompteNoPasswordDtos.add(this.compteMapper.toDto(compte));
        }
        return CompteNoPasswordDtos;
    }
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public CompteDto saveCompte(CompteRequestDto compteDto) {
        Compte compte = this.compteMapper.toEntity(compteDto);
        if (compte != null) {
            compte.setMotDePasse(hashPassword(compte.getMotDePasse()));
            compte = this.compteRepository.save(compte);
        }
        return this.compteMapper.toDto(compte);
    }

    public void deleteCompteById(Long id) {
        this.compteRepository.deleteById(id);
    }

    public CompteDto updateCompte(Long id, CompteRequestDto compteDto) {
        if(!this.compteRepository.existsById(id)) {
            return null;
        }

        Compte compte = this.compteMapper.toEntity(compteDto);
        compte.setId(id);
        compte = this.compteRepository.save(compte);

        return this.compteMapper.toDto(compte);
    }


}
