package com.projetm1.pizzeria.authentification;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.projetm1.pizzeria.authentification.AuthentificationDto;
import com.projetm1.pizzeria.authentification.AuthentificationRepository;
import com.projetm1.pizzeria.authentification.AuthentificationService;
import com.projetm1.pizzeria.compte.Compte;
import com.projetm1.pizzeria.compte.CompteMapper;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import com.projetm1.pizzeria.error.NotFound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthentificationServiceTest {

    @InjectMocks
    private AuthentificationService authentificationService;

    @Mock
    private AuthentificationRepository authentificationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CompteMapper compteMapper;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    // Test de verifyPassword lorsque le mot de passe correspond
    @Test
    public void testVerifyPasswordValid() {
        String rawPassword = "password123";
        String hashedPassword = "hashedPassword";
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);
        assertTrue(authentificationService.verifyPassword(rawPassword, hashedPassword));
    }

    // Test de verifyPassword lorsque le mot de passe ne correspond pas
    @Test
    public void testVerifyPasswordInvalid() {
        String rawPassword = "password123";
        String hashedPassword = "hashedPassword";
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(false);
        assertFalse(authentificationService.verifyPassword(rawPassword, hashedPassword));
    }

    // Test de verifyLogin succès
    @Test
    public void testVerifyLoginSuccess() {
        String username = "user1";
        String rawPassword = "password123";
        String hashedPassword = "hashedPassword";

        AuthentificationDto authDto = new AuthentificationDto();
        authDto.setUsername(username);
        authDto.setPassword(rawPassword);

        Compte compte = new Compte();
        compte.setPseudo(username);
        compte.setMotDePasse(hashedPassword);

        CompteDto compteDto = new CompteDto();
        compteDto.setId(1L);
        compteDto.setPseudo(username);

        when(authentificationRepository.findByPseudo(username)).thenReturn(compte);
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);
        when(compteMapper.toDto(compte)).thenReturn(compteDto);

        CompteDto result = authentificationService.verifyLogin(authDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(username, result.getPseudo());
    }

    // Test de verifyLogin lorsque le compte n'est pas trouvé
    @Test
    public void testVerifyLoginAccountNotFound() {
        String username = "user1";
        String rawPassword = "password123";
        AuthentificationDto authDto = new AuthentificationDto();
        authDto.setUsername(username);
        authDto.setPassword(rawPassword);

        when(authentificationRepository.findByPseudo(username)).thenReturn(null);

        NotFound exception = assertThrows(NotFound.class, () -> authentificationService.verifyLogin(authDto));
        assertEquals("Compte not found", exception.getMessage());
    }

    // Test de verifyLogin lorsque le mot de passe est incorrect
    @Test
    public void testVerifyLoginInvalidPassword() {
        String username = "user1";
        String rawPassword = "password123";
        String hashedPassword = "hashedPassword";

        AuthentificationDto authDto = new AuthentificationDto();
        authDto.setUsername(username);
        authDto.setPassword(rawPassword);

        Compte compte = new Compte();
        compte.setPseudo(username);
        compte.setMotDePasse(hashedPassword);

        when(authentificationRepository.findByPseudo(username)).thenReturn(compte);
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(false);

        NotFound exception = assertThrows(NotFound.class, () -> authentificationService.verifyLogin(authDto));
        assertEquals("Compte not found", exception.getMessage());
    }
}
