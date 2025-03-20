package com.projetm1.pizzeria.compte;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetm1.pizzeria.commande.Commande;
import com.projetm1.pizzeria.commande.CommandeMapper;
import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import com.projetm1.pizzeria.compte.dto.ComptePasswordChangeDto;
import com.projetm1.pizzeria.compte.dto.CompteRequestDto;
import com.projetm1.pizzeria.error.NotFound;
import com.projetm1.pizzeria.error.UnprocessableEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

public class CompteServiceTest {
    @InjectMocks
    private CompteService compteService;

    @Mock
    private CompteRepository compteRepository;

    @Mock
    private com.projetm1.pizzeria.compte.CompteMapper compteMapper;

    @Mock
    private CommandeMapper commandeMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AutoCloseable closeable;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    // Test de getCompteById
    @Test
    public void testGetCompteByIdFound() {
        Long id = 1L;
        Compte compte = new Compte();
        compte.setId(id);
        CompteDto dto = new CompteDto();
        dto.setId(id);

        when(compteRepository.findById(id)).thenReturn(Optional.of(compte));
        when(compteMapper.toDto(compte)).thenReturn(dto);

        CompteDto result = compteService.getCompteById(id);
        assertEquals(id, result.getId());
    }

    @Test
    public void testGetCompteByIdNotFound() {
        Long id = 1L;
        when(compteRepository.findById(id)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> compteService.getCompteById(id));
        assertEquals("Compte not found", exception.getMessage());
    }

    // Test de getCompteByToken
    @Test
    public void testGetCompteByTokenValid() throws JsonProcessingException {
        Long id = 1L;
        CompteDto dto = new CompteDto();
        dto.setId(id);
        String json = objectMapper.writeValueAsString(dto);

        Compte compte = new Compte();
        compte.setId(id);
        when(compteRepository.findById(id)).thenReturn(Optional.of(compte));
        when(compteMapper.toDto(any(Compte.class))).thenReturn(dto);

        CompteDto result = compteService.getCompteByToken(json);
        assertEquals(id, result.getId());
    }

    @Test
    public void testGetCompteByTokenInvalidJson() {
        String invalidJson = "invalid";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> compteService.getCompteByToken(invalidJson));
        assertEquals("Erreur lors de la récupération du compte", exception.getMessage());
    }

    // Test de updateCompteByToken (mise à jour du nom/prénom via token)
    @Test
    public void testUpdateCompteByTokenWithCompteRequestDtoValid() throws JsonProcessingException {
        Long id = 1L;
        // Création du DTO issu du token
        CompteDto tokenDto = new CompteDto();
        tokenDto.setId(id);
        tokenDto.setNom("NomOld");
        tokenDto.setPrenom("PrenomOld");
        String json = objectMapper.writeValueAsString(tokenDto);

        // Création de la demande de mise à jour
        CompteRequestDto requestDto = new CompteRequestDto();
        requestDto.setNom("NomNew");
        requestDto.setPrenom("PrenomNew");

        // Création de l'entité Compte initiale
        Compte compte = new Compte();
        compte.setId(id);
        compte.setNom("NomOld");
        compte.setPrenom("PrenomOld");

        // Simulation de la recherche en base
        when(compteRepository.findById(id)).thenReturn(Optional.of(compte));

        // Après modification, on simule la mise à jour dans l'entité
        // On met à jour l'objet 'compte' pour refléter le changement
        compte.setNom("NomNew");
        compte.setPrenom("PrenomNew");

        // Création d'un nouveau DTO avec les valeurs mises à jour
        CompteDto updatedDto = new CompteDto();
        updatedDto.setId(id);
        updatedDto.setNom("NomNew");
        updatedDto.setPrenom("PrenomNew");

        // Simulation du mapper retournant le nouveau DTO mis à jour
        when(compteMapper.toDto(compte)).thenReturn(updatedDto);

        CompteDto result = compteService.updateCompteByToken(json, requestDto);
        assertEquals("NomNew", result.getNom());
        assertEquals("PrenomNew", result.getPrenom());
    }


    @Test
    public void testUpdateCompteByTokenInvalidJson() {
        String invalidJson = "invalid";
        CompteRequestDto requestDto = new CompteRequestDto();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> compteService.updateCompteByToken(invalidJson, requestDto));
        // L'exception provient de la désérialisation
    }

    // Test de getAllCompte
    @Test
    public void testGetAllCompte() {
        Compte compte = new Compte();
        compte.setId(1L);
        CompteDto dto = new CompteDto();
        dto.setId(1L);

        when(compteRepository.findAll()).thenReturn(List.of(compte));
        when(compteMapper.toDto(compte)).thenReturn(dto);

        assertEquals(1, compteService.getAllCompte().size());
    }

    // Test de hashPassword
    @Test
    public void testHashPassword() {
        String rawPassword = "password";
        String encoded = "encodedPassword";
        when(passwordEncoder.encode(rawPassword)).thenReturn(encoded);
        assertEquals(encoded, compteService.hashPassword(rawPassword));
    }

    // Tests de saveCompte
    @Test
    public void testSaveCompteNullEntity() {
        CompteRequestDto requestDto = new CompteRequestDto();
        // Même si MapStruct ne renvoie normalement pas null, on simule ce cas pour tester la robustesse
        when(compteMapper.toEntity(requestDto)).thenReturn(null);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.saveCompte(requestDto));
        assertEquals("Compte non valide", exception.getMessage());
    }

    @Test
    public void testSaveCompteMissingPseudo() {
        CompteRequestDto requestDto = new CompteRequestDto();
        requestDto.setMotDePasse("pwd");
        requestDto.setNom("Nom");
        requestDto.setPrenom("Prenom");

        // Le mapper ignorera l’ID et les commandes
        Compte compte = new Compte();
        // On simule qu'aucun pseudo n'est renseigné par le mapper
        compte.setPseudo(null);
        when(compteMapper.toEntity(requestDto)).thenReturn(compte);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.saveCompte(requestDto));
        assertEquals("Le Pseudo est obligatoire", exception.getMessage());
    }

    @Test
    public void testSaveCompteMissingMotDePasse() {
        CompteRequestDto requestDto = new CompteRequestDto();
        requestDto.setPseudo("pseudo");
        requestDto.setNom("Nom");
        requestDto.setPrenom("Prenom");

        Compte compte = new Compte();
        compte.setPseudo("pseudo");
        compte.setMotDePasse("");
        when(compteMapper.toEntity(requestDto)).thenReturn(compte);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.saveCompte(requestDto));
        assertEquals("Le mot de passe est obligatoire", exception.getMessage());
    }

    @Test
    public void testSaveCompteMissingNom() {
        CompteRequestDto requestDto = new CompteRequestDto();
        requestDto.setPseudo("pseudo");
        requestDto.setMotDePasse("pwd");
        requestDto.setPrenom("Prenom");

        Compte compte = new Compte();
        compte.setPseudo("pseudo");
        compte.setMotDePasse("pwd");
        compte.setNom("");
        when(compteMapper.toEntity(requestDto)).thenReturn(compte);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.saveCompte(requestDto));
        assertEquals("Le nom est obligatoire", exception.getMessage());
    }

    @Test
    public void testSaveCompteMissingPrenom() {
        CompteRequestDto requestDto = new CompteRequestDto();
        requestDto.setPseudo("pseudo");
        requestDto.setMotDePasse("pwd");
        requestDto.setNom("Nom");

        Compte compte = new Compte();
        compte.setPseudo("pseudo");
        compte.setMotDePasse("pwd");
        compte.setNom("Nom");
        compte.setPrenom("");
        when(compteMapper.toEntity(requestDto)).thenReturn(compte);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.saveCompte(requestDto));
        assertEquals("Le prenom est obligatoire", exception.getMessage());
    }

    @Test
    public void testSaveComptePseudoAlreadyUsed() {
        CompteRequestDto requestDto = new CompteRequestDto();
        requestDto.setPseudo("pseudo");
        requestDto.setMotDePasse("pwd");
        requestDto.setNom("Nom");
        requestDto.setPrenom("Prenom");

        Compte compte = new Compte();
        compte.setPseudo("pseudo");
        compte.setMotDePasse("pwd");
        compte.setNom("Nom");
        compte.setPrenom("Prenom");
        when(compteMapper.toEntity(requestDto)).thenReturn(compte);
        when(compteRepository.existsByPseudo("pseudo")).thenReturn(true);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.saveCompte(requestDto));
        assertEquals("Le pseudo est déjà utilisé", exception.getMessage());
    }

    @Test
    public void testSaveCompteSuccess() {
        CompteRequestDto requestDto = new CompteRequestDto();
        requestDto.setPseudo("pseudo");
        requestDto.setMotDePasse("pwd");
        requestDto.setNom("Nom");
        requestDto.setPrenom("Prenom");

        Compte compte = new Compte();
        compte.setPseudo("pseudo");
        compte.setMotDePasse("pwd");
        compte.setNom("Nom");
        compte.setPrenom("Prenom");

        // Simuler le comportement du mapper MapStruct : l'ID et la liste des commandes ne sont pas renseignés
        when(compteMapper.toEntity(requestDto)).thenReturn(compte);
        when(compteRepository.existsByPseudo("pseudo")).thenReturn(false);
        when(passwordEncoder.encode("pwd")).thenReturn("hashedPwd");

        Compte savedCompte = new Compte();
        savedCompte.setId(1L);
        savedCompte.setPseudo("pseudo");
        savedCompte.setMotDePasse("hashedPwd");
        savedCompte.setNom("Nom");
        savedCompte.setPrenom("Prenom");
        savedCompte.setIsAdmin(false);

        CompteDto dto = new CompteDto();
        dto.setId(1L);
        dto.setPseudo("pseudo");
        dto.setNom("Nom");
        dto.setPrenom("Prenom");
        dto.setIsAdmin(false);

        when(compteRepository.save(any(Compte.class))).thenReturn(savedCompte);
        when(compteMapper.toDto(savedCompte)).thenReturn(dto);

        CompteDto result = compteService.saveCompte(requestDto);
        assertEquals(1L, result.getId());
        assertFalse(result.getIsAdmin());
    }

    // Test de deleteCompteById
    @Test
    public void testDeleteCompteById() {
        Long id = 1L;
        doNothing().when(compteRepository).deleteById(id);
        compteService.deleteCompteById(id);
        verify(compteRepository, times(1)).deleteById(id);
    }

    // Test de updateCompte (mise à jour via l'id)
    @Test
    public void testUpdateCompteNotFound() {
        Long id = 1L;
        CompteRequestDto requestDto = new CompteRequestDto();
        when(compteRepository.findById(id)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> compteService.updateCompte(id, requestDto));
        assertEquals("Compte non trouvé", exception.getMessage());
    }

    @Test
    public void testUpdateCompteSuccess() {
        Long id = 1L;
        Compte compte = new Compte();
        compte.setId(id);
        compte.setIsAdmin(false);

        CompteRequestDto requestDto = new CompteRequestDto();
        requestDto.setIsAdmin(true);

        CompteDto dto = new CompteDto();
        dto.setId(id);
        dto.setIsAdmin(true);

        when(compteRepository.findById(id)).thenReturn(Optional.of(compte));
        when(compteRepository.save(compte)).thenReturn(compte);
        when(compteMapper.toDto(compte)).thenReturn(dto);

        CompteDto result = compteService.updateCompte(id, requestDto);
        assertTrue(result.getIsAdmin());
    }

    // Tests de updateCompteByToken (modification nom/prénom)
    @Test
    public void testUpdateCompteByTokenMissingNom() {
        Long id = 1L;
        CompteDto dto = new CompteDto();
        dto.setId(id);

        CompteRequestDto requestDto = new CompteRequestDto();
        requestDto.setNom("");
        requestDto.setPrenom("Prenom");

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.updateCompteByToken(dto, requestDto));
        assertEquals("Le nom est obligatoire", exception.getMessage());
    }

    @Test
    public void testUpdateCompteByTokenMissingPrenom() {
        Long id = 1L;
        CompteDto dto = new CompteDto();
        dto.setId(id);

        CompteRequestDto requestDto = new CompteRequestDto();
        requestDto.setNom("Nom");
        requestDto.setPrenom("");

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.updateCompteByToken(dto, requestDto));
        assertEquals("Le prenom est obligatoire", exception.getMessage());
    }

    @Test
    public void testUpdateCompteByTokenSuccess() {
        Long id = 1L;
        // DTO issu du token, utilisé uniquement pour récupérer l'ID
        CompteDto tokenDto = new CompteDto();
        tokenDto.setId(id);

        CompteRequestDto requestDto = new CompteRequestDto();
        requestDto.setNom("NomNew");
        requestDto.setPrenom("PrenomNew");

        Compte compte = new Compte();
        compte.setId(id);
        compte.setNom("OldNom");
        compte.setPrenom("OldPrenom");

        when(compteRepository.findById(id)).thenReturn(Optional.of(compte));

        // On simule la sauvegarde et la mise à jour dans l'entité
        compte.setNom(requestDto.getNom());
        compte.setPrenom(requestDto.getPrenom());

        // Création d'un DTO mis à jour pour simuler le comportement du mapper
        CompteDto updatedDto = new CompteDto();
        updatedDto.setId(id);
        updatedDto.setNom(requestDto.getNom());
        updatedDto.setPrenom(requestDto.getPrenom());
        when(compteMapper.toDto(compte)).thenReturn(updatedDto);

        CompteDto result = compteService.updateCompteByToken(tokenDto, requestDto);
        assertEquals("NomNew", result.getNom());
        assertEquals("PrenomNew", result.getPrenom());
    }


    // Tests pour la modification du mot de passe via token
    @Test
    public void testUpdateCompteByTokenPasswordChangeInvalidJson() {
        String invalidJson = "invalid";
        ComptePasswordChangeDto passwordChangeDto = new ComptePasswordChangeDto();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> compteService.updateCompteByToken(invalidJson, passwordChangeDto));
        assertEquals("Erreur lors de la récupération du compte", exception.getMessage());
    }

    @Test
    public void testUpdateComptePasswordChangeMissingNewPassword() throws JsonProcessingException {
        Long id = 1L;
        CompteDto dto = new CompteDto();
        dto.setId(id);
        String json = objectMapper.writeValueAsString(dto);

        ComptePasswordChangeDto passwordChangeDto = new ComptePasswordChangeDto();
        passwordChangeDto.setOldPassword("old");
        passwordChangeDto.setNewPassword("");
        passwordChangeDto.setComfirmPassword("new");

        Compte compte = new Compte();
        compte.setId(id);
        compte.setMotDePasse("hashedOld");
        when(compteRepository.findById(id)).thenReturn(Optional.of(compte));

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.updateCompteByToken(json, passwordChangeDto));
        assertEquals("Le nouveau mot de passe est obligatoire", exception.getMessage());
    }

    @Test
    public void testUpdateComptePasswordChangeMissingOldPassword() throws JsonProcessingException {
        Long id = 1L;
        CompteDto dto = new CompteDto();
        dto.setId(id);
        String json = objectMapper.writeValueAsString(dto);

        ComptePasswordChangeDto passwordChangeDto = new ComptePasswordChangeDto();
        passwordChangeDto.setOldPassword("");
        passwordChangeDto.setNewPassword("new");
        passwordChangeDto.setComfirmPassword("new");

        Compte compte = new Compte();
        compte.setId(id);
        compte.setMotDePasse("hashedOld");
        when(compteRepository.findById(id)).thenReturn(Optional.of(compte));

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.updateCompteByToken(json, passwordChangeDto));
        assertEquals("L'ancien mot de passe est obligatoire", exception.getMessage());
    }

    @Test
    public void testUpdateComptePasswordChangeMissingConfirmPassword() throws JsonProcessingException {
        Long id = 1L;
        CompteDto dto = new CompteDto();
        dto.setId(id);
        String json = objectMapper.writeValueAsString(dto);

        ComptePasswordChangeDto passwordChangeDto = new ComptePasswordChangeDto();
        passwordChangeDto.setOldPassword("old");
        passwordChangeDto.setNewPassword("new");
        passwordChangeDto.setComfirmPassword("");

        Compte compte = new Compte();
        compte.setId(id);
        compte.setMotDePasse("hashedOld");
        when(compteRepository.findById(id)).thenReturn(Optional.of(compte));

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.updateCompteByToken(json, passwordChangeDto));
        assertEquals("La confirmation du mot de passe est obligatoire", exception.getMessage());
    }

    @Test
    public void testUpdateComptePasswordChangeIncorrectOldPassword() throws JsonProcessingException {
        Long id = 1L;
        CompteDto dto = new CompteDto();
        dto.setId(id);
        String json = objectMapper.writeValueAsString(dto);

        ComptePasswordChangeDto passwordChangeDto = new ComptePasswordChangeDto();
        passwordChangeDto.setOldPassword("wrong");
        passwordChangeDto.setNewPassword("new");
        passwordChangeDto.setComfirmPassword("new");

        Compte compte = new Compte();
        compte.setId(id);
        compte.setMotDePasse("hashedOld");
        when(compteRepository.findById(id)).thenReturn(Optional.of(compte));
        when(passwordEncoder.matches("wrong", "hashedOld")).thenReturn(false);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.updateCompteByToken(json, passwordChangeDto));
        assertEquals("Mot de passe incorrect", exception.getMessage());
    }

    @Test
    public void testUpdateComptePasswordChangeSameAsOld() throws JsonProcessingException {
        Long id = 1L;
        CompteDto dto = new CompteDto();
        dto.setId(id);
        String json = objectMapper.writeValueAsString(dto);

        ComptePasswordChangeDto passwordChangeDto = new ComptePasswordChangeDto();
        passwordChangeDto.setOldPassword("old");
        passwordChangeDto.setNewPassword("old");
        passwordChangeDto.setComfirmPassword("old");

        Compte compte = new Compte();
        compte.setId(id);
        compte.setMotDePasse("hashedOld");
        when(compteRepository.findById(id)).thenReturn(Optional.of(compte));
        when(passwordEncoder.matches("old", "hashedOld")).thenReturn(true);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.updateCompteByToken(json, passwordChangeDto));
        assertEquals("Le nouveau mot de passe doit être différent de l'ancien", exception.getMessage());
    }

    @Test
    public void testUpdateComptePasswordChangeMismatchConfirmation() throws JsonProcessingException {
        Long id = 1L;
        CompteDto dto = new CompteDto();
        dto.setId(id);
        String json = objectMapper.writeValueAsString(dto);

        ComptePasswordChangeDto passwordChangeDto = new ComptePasswordChangeDto();
        passwordChangeDto.setOldPassword("old");
        passwordChangeDto.setNewPassword("new");
        passwordChangeDto.setComfirmPassword("mismatch");

        Compte compte = new Compte();
        compte.setId(id);
        compte.setMotDePasse("hashedOld");
        when(compteRepository.findById(id)).thenReturn(Optional.of(compte));
        when(passwordEncoder.matches("old", "hashedOld")).thenReturn(true);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> compteService.updateCompteByToken(json, passwordChangeDto));
        assertEquals("Mot de passe incorrect", exception.getMessage());
    }

    @Test
    public void testUpdateComptePasswordChangeSuccess() throws JsonProcessingException {
        Long id = 1L;
        CompteDto dto = new CompteDto();
        dto.setId(id);
        String json = objectMapper.writeValueAsString(dto);

        ComptePasswordChangeDto passwordChangeDto = new ComptePasswordChangeDto();
        passwordChangeDto.setOldPassword("old");
        passwordChangeDto.setNewPassword("new");
        passwordChangeDto.setComfirmPassword("new");

        Compte compte = new Compte();
        compte.setId(id);
        compte.setMotDePasse("hashedOld");
        when(compteRepository.findById(id)).thenReturn(Optional.of(compte));
        when(passwordEncoder.matches("old", "hashedOld")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("hashedNew");
        when(compteRepository.save(compte)).thenReturn(compte);
        when(compteMapper.toDto(compte)).thenReturn(dto);

        CompteDto result = compteService.updateCompteByToken(json, passwordChangeDto);
        verify(compteRepository, times(1)).save(compte);
        assertEquals(id, result.getId());
    }

    // Test de getCommandesByCompteId
    @Test
    public void testGetCommandesByCompteIdNotFound() {
        Long id = 1L;
        when(compteRepository.findById(id)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> compteService.getCommandesByCompteId(id));
        assertEquals("Compte not found", exception.getMessage());
    }

    @Test
    public void testGetCommandesByCompteIdSuccess() {
        Long id = 1L;
        Compte compte = new Compte();
        compte.setId(id);
        Commande commande = new Commande();
        CommandeDto commandeDto = new CommandeDto();
        compte.setCommandes(Collections.singletonList(commande));

        when(compteRepository.findById(id)).thenReturn(Optional.of(compte));
        when(commandeMapper.toDto(commande)).thenReturn(commandeDto);

        java.util.List<CommandeDto> result = compteService.getCommandesByCompteId(id);
        assertEquals(1, result.size());
    }
}
