package com.projetm1.pizzeria.commande;

import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.commande.dto.CommandeRequestDto;
import com.projetm1.pizzeria.compte.Compte;
import com.projetm1.pizzeria.compte.CompteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("commandeService")
@Transactional
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final CommandeMapper commandeMapper;
    private final CompteRepository compteRepository;

    public CommandeService(CommandeRepository commandeRepository, CommandeMapper commandeMapper, CompteRepository compteRepository) {
        this.commandeRepository = commandeRepository;
        this.commandeMapper = commandeMapper;
        this.compteRepository = compteRepository;
    }

    public CommandeDto getCommandeById(Long id) {
        return this.commandeMapper.toDto(this.commandeRepository.findById(id).orElseThrow());
    }

    public List<CommandeDto> getAllCommandes() {
        return this.commandeRepository.findAll().stream().map(this.commandeMapper::toDto).collect(Collectors.toList());
    }

    public CommandeDto saveCommande(CommandeRequestDto commandeDto) {
        Commande commande = this.commandeMapper.toEntity(commandeDto);
        Compte compte = this.compteRepository.findById(commandeDto.getCompteId()).orElseThrow();
        commande.setCompte(compte);
        commande = this.commandeRepository.save(commande);

        return this.commandeMapper.toDto(commande);
    }

    public void deleteCommandeById(Long id) {
        this.commandeRepository.deleteById(id);
    }

    public CommandeDto updateCommande(Long id, CommandeRequestDto commandeDto) {
        if(!this.commandeRepository.existsById(id)) {
            return null;
        }

        Commande commande = this.commandeMapper.toEntity(commandeDto);
        Compte compte = this.compteRepository.findById(commandeDto.getCompteId()).orElseThrow();
        commande.setCompte(compte);
        commande.setId(id);

        return this.commandeMapper.toDto(this.commandeRepository.save(commande));
    }
}
