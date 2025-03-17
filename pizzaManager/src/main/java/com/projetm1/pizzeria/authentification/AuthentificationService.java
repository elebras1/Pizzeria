package com.projetm1.pizzeria.authentification;

import com.projetm1.pizzeria.compte.Compte;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service("authentificationService")
@Transactional
public class AuthentificationService {
    private final AuthentificationRepository authentificationRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthentificationService(AuthentificationRepository authentificationRepository, PasswordEncoder passwordEncoder) {
        this.authentificationRepository = authentificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    public Boolean verifyLogin(AuthentificationDto authentificationDto) {
        Compte compte = this.authentificationRepository.findByPseudo(authentificationDto.getUsername());
        return compte!=null && verifyPassword(authentificationDto.getPassword(), compte.getMotDePasse());
    }
}
