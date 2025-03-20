package com.projetm1.pizzeria.commande;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetm1.pizzeria.commande.Commande;
import com.projetm1.pizzeria.commande.CommandeMapper;
import com.projetm1.pizzeria.commande.CommandeRepository;
import com.projetm1.pizzeria.commande.CommandeService;
import com.projetm1.pizzeria.commande.dto.CommandeDto;
import com.projetm1.pizzeria.commande.dto.CommandeRequestDto;
import com.projetm1.pizzeria.compte.Compte;
import com.projetm1.pizzeria.compte.CompteRepository;
import com.projetm1.pizzeria.compte.dto.CompteDto;
import com.projetm1.pizzeria.error.NotFound;
import com.projetm1.pizzeria.ingredient.Ingredient;
import com.projetm1.pizzeria.ingredient.IngredientRepository;
import com.projetm1.pizzeria.pizza.Pizza;
import com.projetm1.pizzeria.pizza.PizzaRepository;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanier;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierRequestDto;
import com.stripe.model.checkout.Session;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CommandeServiceTest {

    @InjectMocks
    private CommandeService commandeService;

    @Mock
    private CommandeRepository commandeRepository;

    @Mock
    private CommandeMapper commandeMapper;

    @Mock
    private CompteRepository compteRepository;

    @Mock
    private PizzaRepository pizzaRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    private AutoCloseable closeable;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    // ------------------ Test de getCommandeById ------------------
    @Test
    public void testGetCommandeByIdFound() {
        Long id = 1L;
        Commande commande = new Commande();
        commande.setId(id);
        CommandeDto dto = new CommandeDto();
        dto.setId(id);
        when(commandeRepository.findById(id)).thenReturn(Optional.of(commande));
        when(commandeMapper.toDto(commande)).thenReturn(dto);

        CommandeDto result = commandeService.getCommandeById(id);
        assertEquals(id, result.getId());
    }

    @Test
    public void testGetCommandeByIdNotFound() {
        Long id = 1L;
        when(commandeRepository.findById(id)).thenReturn(Optional.empty());
        NotFound exception = assertThrows(NotFound.class, () -> commandeService.getCommandeById(id));
        assertEquals("Commande introuvable", exception.getMessage());
    }

    // ------------------ Test de getAllCommandes ------------------
    @Test
    public void testGetAllCommandes() {
        Commande commande1 = new Commande();
        commande1.setId(1L);
        Commande commande2 = new Commande();
        commande2.setId(2L);
        CommandeDto dto1 = new CommandeDto();
        dto1.setId(1L);
        CommandeDto dto2 = new CommandeDto();
        dto2.setId(2L);
        when(commandeRepository.findAll()).thenReturn(Arrays.asList(commande1, commande2));
        when(commandeMapper.toDto(commande1)).thenReturn(dto1);
        when(commandeMapper.toDto(commande2)).thenReturn(dto2);

        List<CommandeDto> dtos = commandeService.getAllCommandes();
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals(2L, dtos.get(1).getId());
    }

    // ------------------ Test de saveCommande ------------------
    @Test
    public void testSaveCommandeSuccess() throws JsonProcessingException {
        Long compteId = 1L;
        // Simuler la désérialisation du compte via JSON
        CompteDto compteDto = new CompteDto();
        compteDto.setId(compteId);
        String compteJson = objectMapper.writeValueAsString(compteDto);

        // Création de la commande via DTO
        CommandeRequestDto commandeRequestDto = new CommandeRequestDto();
        // Simuler un panier avec un PizzaPanierRequestDto
        PizzaPanierRequestDto pizzaPanierRequestDto = new PizzaPanierRequestDto();
        pizzaPanierRequestDto.setPizzaId(10L);
        Set<Long> ingredientsIds = new HashSet<>();
        ingredientsIds.add(100L);
        ingredientsIds.add(101L);
        pizzaPanierRequestDto.setIngredientsIds(ingredientsIds);
        commandeRequestDto.setPanier(List.of(pizzaPanierRequestDto));

        // Simuler qu'aucune commande en cours n'existe pour ce compte
        when(commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compteId))
                .thenReturn(Optional.empty());

        // Simuler la conversion du DTO en entité
        Commande commande = new Commande();
        commande.setPanier(new ArrayList<>());
        when(commandeMapper.toEntity(commandeRequestDto)).thenReturn(commande);

        // Simuler la récupération du compte
        Compte compte = new Compte();
        compte.setId(compteId);
        when(compteRepository.findById(compteId)).thenReturn(Optional.of(compte));

        // Simuler la récupération de la pizza
        Pizza pizza = new Pizza();
        pizza.setId(10L);
        when(pizzaRepository.findById(10L)).thenReturn(Optional.of(pizza));

        // Simuler la récupération des ingrédients
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(100L);
        ingredient1.setPrix(2.0f);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(101L);
        ingredient2.setPrix(3.0f);
        when(ingredientRepository.findById(100L)).thenReturn(Optional.of(ingredient1));
        when(ingredientRepository.findById(101L)).thenReturn(Optional.of(ingredient2));

        // Simuler la sauvegarde de la commande et le mapping en DTO
        commande.setId(1L);
        CommandeDto savedDto = new CommandeDto();
        savedDto.setId(1L);
        when(commandeRepository.save(any(Commande.class))).thenReturn(commande);
        when(commandeMapper.toDto(commande)).thenReturn(savedDto);

        CommandeDto result = commandeService.saveCommande(compteJson, commandeRequestDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testSaveCommandeJsonProcessingException() {
        String invalidJson = "invalid";
        CommandeRequestDto commandeRequestDto = new CommandeRequestDto();
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> commandeService.saveCommande(invalidJson, commandeRequestDto));
        assertEquals("Erreur lors de la création de la commande", exception.getMessage());
    }

    // ------------------ Test de saveCommande : Commande en cours déjà existante ------------------
    @Test
    public void testSaveCommandeCommandeEnCoursAlreadyExists() throws JsonProcessingException {
        Long compteId = 1L;
        // Simuler la désérialisation du compte via JSON
        CompteDto compteDto = new CompteDto();
        compteDto.setId(compteId);
        String compteJson = objectMapper.writeValueAsString(compteDto);

        CommandeRequestDto commandeRequestDto = new CommandeRequestDto();
        // Simuler un panier avec un PizzaPanierRequestDto
        PizzaPanierRequestDto pizzaPanierRequestDto = new PizzaPanierRequestDto();
        pizzaPanierRequestDto.setPizzaId(10L);
        Set<Long> ingredientsIds = new HashSet<>();
        ingredientsIds.add(100L);
        ingredientsIds.add(101L);
        pizzaPanierRequestDto.setIngredientsIds(ingredientsIds);
        commandeRequestDto.setPanier(Arrays.asList(pizzaPanierRequestDto));

        // Simuler qu'une commande en cours existe déjà pour ce compte
        Commande existingCommande = new Commande();
        existingCommande.setId(2L);
        when(commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compteId))
                .thenReturn(Optional.of(existingCommande));

        NotFound exception = assertThrows(NotFound.class,
                () -> commandeService.saveCommande(compteJson, commandeRequestDto));
        assertEquals("Commande en cours déjà existante", exception.getMessage());
    }

    // ------------------ Test de saveCommande : Compte introuvable ------------------
    @Test
    public void testSaveCommandeCompteIntrouvable() throws JsonProcessingException {
        Long compteId = 1L;
        // Simuler la désérialisation du compte via JSON
        CompteDto compteDto = new CompteDto();
        compteDto.setId(compteId);
        String compteJson = objectMapper.writeValueAsString(compteDto);

        CommandeRequestDto commandeRequestDto = new CommandeRequestDto();
        // Simuler un panier avec un PizzaPanierRequestDto
        PizzaPanierRequestDto pizzaPanierRequestDto = new PizzaPanierRequestDto();
        pizzaPanierRequestDto.setPizzaId(10L);
        Set<Long> ingredientsIds = new HashSet<>();
        ingredientsIds.add(100L);
        ingredientsIds.add(101L);
        pizzaPanierRequestDto.setIngredientsIds(ingredientsIds);
        commandeRequestDto.setPanier(Arrays.asList(pizzaPanierRequestDto));

        // Simuler qu'aucune commande en cours n'existe
        when(commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compteId))
                .thenReturn(Optional.empty());
        // Simuler la conversion du DTO en entité
        Commande commande = new Commande();
        commande.setPanier(new ArrayList<>());
        when(commandeMapper.toEntity(commandeRequestDto)).thenReturn(commande);
        // Simuler que le compte n'est pas trouvé
        when(compteRepository.findById(compteId))
                .thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class,
                () -> commandeService.saveCommande(compteJson, commandeRequestDto));
        assertEquals("Compte introuvable", exception.getMessage());
    }

    // ------------------ Test de getCommandeEnCoursByCompteId (version String) ------------------
    @Test
    public void testGetCommandeEnCoursByCompteIdFound() throws JsonProcessingException {
        Long compteId = 1L;
        CompteDto compteDto = new CompteDto();
        compteDto.setId(compteId);
        String compteJson = objectMapper.writeValueAsString(compteDto);

        Commande commande = new Commande();
        commande.setId(1L);
        CommandeDto commandeDto = new CommandeDto();
        commandeDto.setId(1L);
        when(commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compteId))
                .thenReturn(Optional.of(commande));
        when(commandeMapper.toDto(commande)).thenReturn(commandeDto);

        CommandeDto result = commandeService.getCommandeEnCoursByCompteId(compteJson);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetCommandeEnCoursByCompteIdNotFound() throws JsonProcessingException {
        Long compteId = 1L;
        CompteDto compteDto = new CompteDto();
        compteDto.setId(compteId);
        String compteJson = objectMapper.writeValueAsString(compteDto);

        when(commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compteId))
                .thenReturn(Optional.empty());
        CommandeDto result = commandeService.getCommandeEnCoursByCompteId(compteJson);
        assertNull(result);
    }

    @Test
    public void testGetCommandeEnCoursByCompteIdJsonProcessingException() {
        String invalidJson = "invalid";
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                commandeService.getCommandeEnCoursByCompteId(invalidJson)
        );
        assertEquals("Erreur lors de la récupération de la commande", exception.getMessage());
    }

    // ------------------ Test de deleteCommandeById ------------------
    @Test
    public void testDeleteCommandeByIdSuccess() {
        Long id = 1L;
        Commande commande = new Commande();
        commande.setId(id);
        when(commandeRepository.findById(id)).thenReturn(Optional.of(commande));
        doNothing().when(commandeRepository).deleteById(id);

        commandeService.deleteCommandeById(id);
        verify(commandeRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteCommandeByIdNotFound() {
        Long id = 1L;
        when(commandeRepository.findById(id)).thenReturn(Optional.empty());
        NotFound exception = assertThrows(NotFound.class, () -> commandeService.deleteCommandeById(id));
        assertEquals("Commande introuvable", exception.getMessage());
    }

    // ------------------ Test de finishCommande ------------------
    @Test
    public void testFinishCommandeSuccess() {
        Long id = 1L;
        Commande commande = new Commande();
        commande.setId(id);
        commande.setEnCours(true);
        CommandeDto commandeDto = new CommandeDto();
        commandeDto.setId(id);
        when(commandeRepository.findById(id)).thenReturn(Optional.of(commande));
        // Simuler la modification de l'état de la commande
        commande.setEnCours(false);
        when(commandeRepository.save(commande)).thenReturn(commande);
        when(commandeMapper.toDto(commande)).thenReturn(commandeDto);

        CommandeDto result = commandeService.finishCommande(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void testFinishCommandeNotFound() {
        Long id = 1L;
        when(commandeRepository.findById(id)).thenReturn(Optional.empty());
        NotFound exception = assertThrows(NotFound.class, () -> commandeService.finishCommande(id));
        assertEquals("Commande introuvable", exception.getMessage());
    }

    // ------------------ Test de updateCommande ------------------
    @Test
    public void testUpdateCommandeSuccess() throws JsonProcessingException {
        Long compteId = 1L;
        CompteDto compteDto = new CompteDto();
        compteDto.setId(compteId);
        String compteJson = objectMapper.writeValueAsString(compteDto);

        CommandeRequestDto commandeRequestDto = new CommandeRequestDto();
        commandeRequestDto.setPanier(new ArrayList<>());
        // Simuler une commande en cours existante pour ce compte
        Commande commande = new Commande();
        commande.setId(1L);
        commande.setPanier(new ArrayList<>());
        when(commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compteId))
                .thenReturn(Optional.of(commande));

        // Après mise à jour du panier, la commande est sauvegardée et mappée en DTO
        CommandeDto updatedDto = new CommandeDto();
        updatedDto.setId(commande.getId());
        when(commandeRepository.save(commande)).thenReturn(commande);
        when(commandeMapper.toDto(commande)).thenReturn(updatedDto);

        CommandeDto result = commandeService.updateCommande(compteJson, commandeRequestDto);
        assertNotNull(result);
        assertEquals(commande.getId(), result.getId());
    }

    @Test
    public void testUpdateCommandeCommandeNotFound() throws JsonProcessingException {
        Long compteId = 1L;
        CompteDto compteDto = new CompteDto();
        compteDto.setId(compteId);
        String compteJson = objectMapper.writeValueAsString(compteDto);

        CommandeRequestDto commandeRequestDto = new CommandeRequestDto();
        // On simule un panier vide ou tout autre contenu minimal requis
        commandeRequestDto.setPanier(new ArrayList<>());

        // Simuler qu'aucune commande en cours n'existe pour ce compte
        when(commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compteId))
                .thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class,
                () -> commandeService.updateCommande(compteJson, commandeRequestDto));
        assertEquals("Commande introuvable", exception.getMessage());
    }

    @Test
    public void testUpdateCommandeJsonProcessingException() {
        String invalidJson = "invalid";
        CommandeRequestDto commandeRequestDto = new CommandeRequestDto();
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> commandeService.updateCommande(invalidJson, commandeRequestDto));
        assertEquals("Erreur lors de la mise à jour de la commande", exception.getMessage());
    }

    // ------------------ Test de createCheckoutSession ------------------
    @Test
    public void testCreateCheckoutSessionCommandeNotFound() throws JsonProcessingException {
        Long compteId = 1L;
        CompteDto compteDto = new CompteDto();
        compteDto.setId(compteId);
        String compteJson = objectMapper.writeValueAsString(compteDto);
        when(commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compteId))
                .thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> commandeService.createCheckoutSession(compteJson));
        assertEquals("Commande introuvable", exception.getMessage());
    }


    @Test
    public void testCreateCheckoutSessionZeroAmount() throws JsonProcessingException {
        Long compteId = 1L;
        CompteDto compteDto = new CompteDto();
        compteDto.setId(compteId);
        String compteJson = objectMapper.writeValueAsString(compteDto);

        // Simuler une commande avec un panier vide, donc montant total égal à 0
        Commande commande = new Commande();
        commande.setId(1L);
        commande.setPanier(new ArrayList<>());
        when(commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compteId))
                .thenReturn(Optional.of(commande));

        ResponseEntity<Map<String, String>> response = commandeService.createCheckoutSession(compteJson);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("http://localhost:5173/panier", response.getBody().get("url"));
    }

    // ------------------ Test de payerSuccess ------------------
    @Test
    public void testPayerSuccessSuccess() throws JsonProcessingException {
        Long compteId = 1L;
        CompteDto compteDto = new CompteDto();
        compteDto.setId(compteId);
        String compteJson = objectMapper.writeValueAsString(compteDto);

        // Simuler une commande en cours existante
        Commande commande = new Commande();
        commande.setId(1L);
        when(commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compteId))
                .thenReturn(Optional.of(commande));
        // Simuler que la méthode privée payCommande met à jour la commande
        commande.setIsPaye(true);
        when(commandeRepository.save(any(Commande.class))).thenReturn(commande);

        when(commandeRepository.findById(commande.getId())).thenReturn(Optional.of(commande));

        Boolean result = commandeService.payerSuccess(compteJson);
        assertTrue(result);
    }

    @Test
    public void testPayerSuccessCommandeNotFound() throws JsonProcessingException {
        Long compteId = 1L;
        CompteDto compteDto = new CompteDto();
        compteDto.setId(compteId);
        String compteJson = objectMapper.writeValueAsString(compteDto);

        when(commandeRepository.findByCompteIdAndEnCoursTrueAndIsPayeFalse(compteId))
                .thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class,
                () -> commandeService.payerSuccess(compteJson));
        assertEquals("Commande introuvable", exception.getMessage());
    }

    @Test
    public void testPayerSuccessJsonProcessingException() {
        String invalidJson = "invalid";
        NotFound exception = assertThrows(NotFound.class, () -> commandeService.payerSuccess(invalidJson));
        assertEquals("Erreur lors de la récupération de la commande", exception.getMessage());
    }

}
