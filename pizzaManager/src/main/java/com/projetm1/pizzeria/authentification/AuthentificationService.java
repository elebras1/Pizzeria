package com.projetm1.pizzeria.authentification;

import com.projetm1.pizzeria.compte.Compte;
import com.projetm1.pizzeria.compte.CompteMapper;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import com.projetm1.pizzeria.error.NotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service("authentificationService")
@Transactional
public class AuthentificationService {
    private final AuthentificationRepository authentificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompteMapper compteMapper;

    public AuthentificationService(AuthentificationRepository authentificationRepository, PasswordEncoder passwordEncoder, CompteMapper compteMapper) {
        this.authentificationRepository = authentificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.compteMapper = compteMapper;
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    public CompteDto verifyLogin(AuthentificationDto authentificationDto) {
        Compte compte = this.authentificationRepository.findByPseudo(authentificationDto.getUsername());
        if(compte!=null && verifyPassword(authentificationDto.getPassword(), compte.getMotDePasse())){
            return this.compteMapper.toDto(compte);
        }else{
            throw new NotFound("Compte not found");
        }
    }

}
