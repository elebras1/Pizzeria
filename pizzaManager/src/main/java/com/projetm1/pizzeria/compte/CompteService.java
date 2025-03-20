package com.projetm1.pizzeria.compte;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetm1.pizzeria.commande.Commande;
import com.projetm1.pizzeria.commande.CommandeMapper;
import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.compte.dto.ComptePasswordChangeDto;
import com.projetm1.pizzeria.compte.dto.CompteRequestDto;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import com.projetm1.pizzeria.error.NotFound;
import com.projetm1.pizzeria.error.UnprocessableEntity;
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
        return this.compteMapper.toDto(this.compteRepository.findById(id).orElseThrow( () -> new NotFound("Compte not found")));
    }
    public CompteDto getCompteByToken(String compteJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compteDto = objectMapper.readValue(compteJson, CompteDto.class);
            return this.getCompteById(compteDto.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la récupération du compte");
        }
    }
    public CompteDto updateCompteByToken(String compteJson, CompteRequestDto compteRequestDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compteDto = objectMapper.readValue(compteJson, CompteDto.class);
            return this.updateCompteByToken(compteDto,compteRequestDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
        if(compte==null){
            throw new UnprocessableEntity("Compte non valide");
        }
        if(compte.getPseudo() ==null || compte.getPseudo().isEmpty()) {
            throw new UnprocessableEntity("Le Pseudo est obligatoire");
        }
        if(compte.getMotDePasse() ==null || compte.getMotDePasse().isEmpty()) {
            throw new UnprocessableEntity("Le mot de passe est obligatoire");
        }
        if(compte.getNom() ==null || compte.getNom().isEmpty()) {
            throw new UnprocessableEntity("Le nom est obligatoire");
        }
        if(compte.getPrenom() ==null || compte.getPrenom().isEmpty()) {
            throw new UnprocessableEntity("Le prenom est obligatoire");
        }
        if(this.compteRepository.existsByPseudo(compte.getPseudo())) {
            throw new UnprocessableEntity("Le pseudo est déjà utilisé");
        }
        compte.setMotDePasse(hashPassword(compte.getMotDePasse()));
        compte.setIsAdmin(false);
        compte = this.compteRepository.save(compte);

        return this.compteMapper.toDto(compte);
    }

    public void deleteCompteById(Long id) {
        this.compteRepository.deleteById(id);
    }

    public CompteDto updateCompte(Long id, CompteRequestDto compteDto) {
        Compte compte = this.compteRepository.findById(id).orElseThrow(()-> new NotFound("Compte non trouvé"));
        compte.setIsAdmin(compteDto.getIsAdmin());
        compte = this.compteRepository.save(compte);
        return this.compteMapper.toDto(compte);
    }
    public CompteDto updateCompteByToken(CompteDto compteDto, CompteRequestDto compteRequestDto){
        if(compteRequestDto.getNom() ==null || compteRequestDto.getNom().isEmpty()) {
            throw new UnprocessableEntity("Le nom est obligatoire");
        }
        if(compteRequestDto.getPrenom() ==null || compteRequestDto.getPrenom().isEmpty()) {
            throw new UnprocessableEntity("Le prenom est obligatoire");
        }
        Compte compte = this.compteRepository.findById(compteDto.getId()).orElseThrow(()-> new NotFound("Compte non trouvé"));
        compte.setNom(compteRequestDto.getNom());
        compte.setPrenom(compteRequestDto.getPrenom());
        this.compteRepository.save(compte);
        return this.compteMapper.toDto(compte);
    }
    public CompteDto updateCompteByToken(String compteJson, ComptePasswordChangeDto comptePasswordChangeDto){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompteDto compteDto = objectMapper.readValue(compteJson, CompteDto.class);
            return this.updateComptePasswordByToken(compteDto,comptePasswordChangeDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la récupération du compte");
        }
    }
    private CompteDto updateComptePasswordByToken(CompteDto compteDto, ComptePasswordChangeDto comptePasswordChangeDto){
        if(comptePasswordChangeDto.getNewPassword() ==null || comptePasswordChangeDto.getNewPassword().isEmpty()) {
            throw new UnprocessableEntity("Le nouveau mot de passe est obligatoire");
        }
        if(comptePasswordChangeDto.getOldPassword() ==null || comptePasswordChangeDto.getOldPassword().isEmpty()) {
            throw new UnprocessableEntity("L'ancien mot de passe est obligatoire");
        }
        if(comptePasswordChangeDto.getComfirmPassword() ==null || comptePasswordChangeDto.getComfirmPassword().isEmpty()) {
            throw new UnprocessableEntity("La confirmation du mot de passe est obligatoire");
        }
        Compte compte = this.compteRepository.findById(compteDto.getId()).orElseThrow(() -> new NotFound("Compte non trouvé"));
        if(!passwordEncoder.matches(comptePasswordChangeDto.getOldPassword(), compte.getMotDePasse())) {
            throw new UnprocessableEntity("Mot de passe incorrect");
        }
        if(comptePasswordChangeDto.getOldPassword().equals(comptePasswordChangeDto.getNewPassword())) {
            throw new UnprocessableEntity("Le nouveau mot de passe doit être différent de l'ancien");
        }
        if(!comptePasswordChangeDto.getNewPassword().equals(comptePasswordChangeDto.getComfirmPassword())) {
            throw new UnprocessableEntity("Mot de passe incorrect");
        }
        compte.setMotDePasse(hashPassword(comptePasswordChangeDto.getNewPassword()));
        this.compteRepository.save(compte);
        return compteDto;
    }

    public List<CommandeDto> getCommandesByCompteId(Long id) {
        Compte compte = this.compteRepository.findById(id).orElseThrow(() -> new NotFound("Compte not found"));
        List<CommandeDto> commandeDtos = new ArrayList<>();
        for (Commande commande : compte.getCommandes()) {
            commandeDtos.add(this.commandeMapper.toDto(commande));
        }
        return commandeDtos;
    }
}
