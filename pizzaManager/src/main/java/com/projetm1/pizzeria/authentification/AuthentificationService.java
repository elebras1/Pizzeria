package com.projetm1.pizzeria.authentification;

import com.projetm1.pizzeria.Compte.Compte;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("authentificationService")
@Transactional
public class AuthentificationService {
    private final AuthentificationRepository authentificationRepository;

    public AuthentificationService(AuthentificationRepository authentificationRepository) {
        this.authentificationRepository = authentificationRepository;
    }

    public Boolean verifyLogin(AuthentificationDto authentificationDto) {
        Compte compte = this.authentificationRepository.findByNom(authentificationDto.getUsername());
        return compte != null && compte.getMotDePasse().equals(authentificationDto.getPassword());
    }
}
