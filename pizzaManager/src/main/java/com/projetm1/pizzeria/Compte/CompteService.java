package com.projetm1.pizzeria.Compte;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;

@Service("compteService")
@Transactional
public class CompteService {
    private final CompteRepository compteRepository;
    private final CompteMapper compteMapper;
    private final PasswordEncoder passwordEncoder;
    private final CompteNoPasswordMapper compteNoPasswordMapper;

    public CompteService(CompteRepository compteRepository, CompteMapper compteMapper, PasswordEncoder passwordEncoder, CompteNoPasswordMapper compteNoPasswordMapper) {
        this.compteRepository = compteRepository;
        this.compteMapper = compteMapper;
        this.passwordEncoder = passwordEncoder;
        this.compteNoPasswordMapper = compteNoPasswordMapper;
    }

    public CompteNoPasswordDto getCompteById(Long id) {
        return compteNoPasswordMapper.toDto(this.compteRepository.findById(id).orElse(null));
    }

    public List<CompteNoPasswordDto> getAllCompte() {
        List<CompteNoPasswordDto> CompteNoPasswordDtos = new ArrayList<>();
        for(Compte compte : this.compteRepository.findAll()) {
            CompteNoPasswordDtos.add(this.compteNoPasswordMapper.toDto(compte));
        }
        return CompteNoPasswordDtos;
    }
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public CompteNoPasswordDto saveCompte(CompteDto compteDto) {
        Compte compte = this.compteMapper.toEntity(compteDto);
        if (compte != null) {
            compte.setMotDePasse(hashPassword(compte.getMotDePasse()));
            compte = this.compteRepository.save(compte);
        }
        return this.compteNoPasswordMapper.toDto(compte);
    }

    public void deleteCompteById(Long id) {
        this.compteRepository.deleteById(id);
    }

    public CompteNoPasswordDto updateCompte(CompteDto compteDto) {
        Compte compte = this.compteMapper.toEntity(compteDto);
        compte = this.compteRepository.save(compte);
        return this.compteNoPasswordMapper.toDto(compte);
    }


}
