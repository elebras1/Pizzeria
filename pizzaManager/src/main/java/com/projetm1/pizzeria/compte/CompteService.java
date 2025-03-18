package com.projetm1.pizzeria.compte;

import com.projetm1.pizzeria.commande.Commande;
import com.projetm1.pizzeria.commande.CommandeMapper;
import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.compte.dto.ComptePasswordChangeDto;
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
    private final CommandeMapper commandeMapper;
    private final PasswordEncoder passwordEncoder;

    public CompteService(CompteRepository compteRepository, CompteMapper compteMapper, CommandeMapper commandeMapper, PasswordEncoder passwordEncoder) {
        this.compteRepository = compteRepository;
        this.compteMapper = compteMapper;
        this.commandeMapper = commandeMapper;
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
    public CompteDto updateCompteByToken(CompteDto compteDto, CompteRequestDto compteRequestDto){
        System.out.println(compteRequestDto);
        Compte compte = this.compteRepository.findById(compteDto.getId()).orElse(null);
        if (compte == null) {
            return null;
        }
        compte.setNom(compteRequestDto.getNom());
        compte.setPrenom(compteRequestDto.getPrenom());
        this.compteRepository.save(compte);
        return compteDto;
    }
    public CompteDto updateComptePasswordByToken(CompteDto compteDto, ComptePasswordChangeDto comptePasswordChangeDto){
        Compte compte = this.compteRepository.findById(compteDto.getId()).orElse(null);
        if (compte == null) {
            return null;
        }
        if(!passwordEncoder.matches(comptePasswordChangeDto.getOldPassword(), compte.getMotDePasse())) {
            return null;
        }
        if(comptePasswordChangeDto.getOldPassword().equals(comptePasswordChangeDto.getNewPassword())) {
            return null;
        }
        if(!comptePasswordChangeDto.getNewPassword().equals(comptePasswordChangeDto.getComfirmPassword())) {
            return null;
        }
        compte.setMotDePasse(hashPassword(comptePasswordChangeDto.getNewPassword()));
        this.compteRepository.save(compte);
        return compteDto;
    }

    public List<CommandeDto> getCommandesByCompteId(Long id) {
        Compte compte = this.compteRepository.findById(id).orElse(null);
        if (compte == null) {
            return null;
        }
        List<CommandeDto> commandeDtos = new ArrayList<>();
        for (Commande commande : compte.getCommandes()) {
            commandeDtos.add(this.commandeMapper.toDto(commande));
        }
        return commandeDtos;
    }
}
