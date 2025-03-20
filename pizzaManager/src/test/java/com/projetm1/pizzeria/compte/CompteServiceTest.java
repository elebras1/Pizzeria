package com.projetm1.pizzeria.compte;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.projetm1.pizzeria.commande.Commande;
import com.projetm1.pizzeria.commande.CommandeMapper;
import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import com.projetm1.pizzeria.compte.dto.ComptePasswordChangeDto;
import com.projetm1.pizzeria.compte.dto.CompteRequestDto;
import com.projetm1.pizzeria.error.UnprocessableEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompteServiceTest {

    @Mock
    private CompteRepository compteRepository;

    @Mock
    private CompteMapper compteMapper;

    @Mock
    private CommandeMapper commandeMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CompteService compteService;
    private Compte compte;
    private CompteDto compteDto;
    private CompteRequestDto compteRequestDto;
    private ComptePasswordChangeDto comptePasswordChangeDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        compte = new Compte();
        compte.setId(1L);
        compte.setNom("Dupont");
        compte.setPrenom("Jean");
        compte.setMotDePasse("password");
        compte.setIsAdmin(false);

        compteDto = new CompteDto();
        compteDto.setId(1L);
        compteDto.setNom("Dupont");
        compteDto.setPrenom("Jean");

        compteRequestDto = new CompteRequestDto();
        compteRequestDto.setNom("Dupont");
        compteRequestDto.setPrenom("Jean");
        compteRequestDto.setMotDePasse("newpassword");
        compteRequestDto.setIsAdmin(false);

        comptePasswordChangeDto = new ComptePasswordChangeDto();
        comptePasswordChangeDto.setOldPassword("password");
        comptePasswordChangeDto.setNewPassword("newpassword");
        comptePasswordChangeDto.setComfirmPassword("newpassword");
    }

    @Test
    public void testGetCompteById() {
        when(compteRepository.findById(1L)).thenReturn(Optional.of(compte));
        when(compteMapper.toDto(compte)).thenReturn(compteDto);

        CompteDto result = compteService.getCompteById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testSaveCompte() {
        Compte compteToSave = new Compte();
        compteToSave.setMotDePasse(compteRequestDto.getMotDePasse());
        compteToSave.setIsAdmin(false);

        when(compteMapper.toEntity(compteRequestDto)).thenReturn(compteToSave);
        when(passwordEncoder.encode(compteRequestDto.getMotDePasse())).thenReturn("hashedNewPassword");

        compteToSave.setMotDePasse("hashedNewPassword");
        when(compteRepository.save(compteToSave)).thenReturn(compteToSave);
        when(compteMapper.toDto(compteToSave)).thenReturn(compteDto);

        CompteDto result = compteService.saveCompte(compteRequestDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testUpdateCompteByToken() {
        String compteJson = "{\"id\":1,\"nom\":\"Dupont\",\"prenom\":\"Jean\"}";

        when(compteRepository.findById(1L)).thenReturn(Optional.of(compte));
        compte.setNom("Martin");
        compte.setPrenom("Paul");
        when(compteRepository.save(compte)).thenReturn(compte);

        compteDto.setNom("Martin");
        compteDto.setPrenom("Paul");
        when(compteMapper.toDto(compte)).thenReturn(compteDto);

        CompteRequestDto updateRequest = new CompteRequestDto();
        updateRequest.setNom("Martin");
        updateRequest.setPrenom("Paul");

        CompteDto result = compteService.updateCompteByToken(compteJson, updateRequest);
        assertNotNull(result);
        assertEquals("Martin", result.getNom());
        assertEquals("Paul", result.getPrenom());
    }

    @Test
    public void testUpdateComptePasswordByToken() {
        String compteJson = "{\"id\":1,\"nom\":\"Dupont\",\"prenom\":\"Jean\"}";

        when(compteRepository.findById(1L)).thenReturn(Optional.of(compte));
        when(passwordEncoder.matches("password", compte.getMotDePasse())).thenReturn(true);
        when(passwordEncoder.encode("newpassword")).thenReturn("hashedNewPassword");

        CompteDto result = compteService.updateCompteByToken(compteJson, comptePasswordChangeDto);
        assertNotNull(result);

        comptePasswordChangeDto.setOldPassword("newpassword");
        assertThrows(UnprocessableEntity.class, () -> compteService.updateCompteByToken(compteJson, comptePasswordChangeDto));
        comptePasswordChangeDto.setOldPassword("password");

        comptePasswordChangeDto.setNewPassword("new4564password");
        assertThrows(UnprocessableEntity.class, () -> compteService.updateCompteByToken(compteJson, comptePasswordChangeDto));

        comptePasswordChangeDto.setNewPassword("new4564password");
        assertThrows(UnprocessableEntity.class, () -> compteService.updateCompteByToken(compteJson, comptePasswordChangeDto));


    }

    @Test
    public void testGetAllCompte() {
        List<Compte> comptes = new ArrayList<>();
        comptes.add(compte);
        when(compteRepository.findAll()).thenReturn(comptes);
        when(compteMapper.toDto(compte)).thenReturn(compteDto);

        List<CompteDto> result = compteService.getAllCompte();
        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteCompteById() {
        compteService.deleteCompteById(1L);
        verify(compteRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateCompte() {
        when(compteRepository.existsById(1L)).thenReturn(true);
        when(compteRepository.findById(1L)).thenReturn(Optional.of(compte));

        compte.setIsAdmin(true);
        when(compteRepository.save(compte)).thenReturn(compte);
        compteDto.setIsAdmin(true);
        when(compteMapper.toDto(compte)).thenReturn(compteDto);

        CompteDto result = compteService.updateCompte(1L, compteRequestDto);
        assertNotNull(result);
        assertTrue(result.getIsAdmin());
    }

    @Test
    public void testGetCommandesByCompteId() {
        Commande commande = new Commande();
        List<Commande> commandes = new ArrayList<>();
        commandes.add(commande);
        compte.setCommandes(commandes);

        when(compteRepository.findById(1L)).thenReturn(Optional.of(compte));
        CommandeDto commandeDto = new CommandeDto();
        when(commandeMapper.toDto(commande)).thenReturn(commandeDto);

        List<CommandeDto> result = compteService.getCommandesByCompteId(1L);
        assertEquals(1, result.size());
    }
}
