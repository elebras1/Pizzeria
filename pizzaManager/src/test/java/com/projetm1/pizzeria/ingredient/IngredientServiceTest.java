package com.projetm1.pizzeria.ingredient;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.projetm1.pizzeria.error.Conflict;
import com.projetm1.pizzeria.error.NotFound;
import com.projetm1.pizzeria.ingredient.dto.IngredientDto;
import com.projetm1.pizzeria.ingredient.dto.IngredientRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class IngredientServiceTest {
    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    // Tests pour getIngredientById
    @Test
    public void testGetIngredientByIdFound() {
        // Préparation
        Long id = 1L;
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        IngredientDto dto = new IngredientDto();
        dto.setId(id);

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));
        when(ingredientMapper.toDto(ingredient)).thenReturn(dto);

        IngredientDto result = ingredientService.getIngredientById(id);

        assertEquals(id, result.getId());
        verify(ingredientRepository).findById(id);
        verify(ingredientMapper).toDto(ingredient);
    }

    @Test
    public void testGetIngredientByIdNotFound() {
        Long id = 1L;
        when(ingredientRepository.findById(id)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> ingredientService.getIngredientById(id));
        assertEquals("L'ingrédient n'existe pas", exception.getMessage());
        verify(ingredientRepository).findById(id);
    }

    // Tests pour getAllIngredients
    @Test
    public void testGetAllIngredientsWithData() {
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);

        IngredientDto dto1 = new IngredientDto();
        dto1.setId(1L);
        IngredientDto dto2 = new IngredientDto();
        dto2.setId(2L);

        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);

        when(ingredientRepository.findAll()).thenReturn(ingredients);
        when(ingredientMapper.toDto(ingredient1)).thenReturn(dto1);
        when(ingredientMapper.toDto(ingredient2)).thenReturn(dto2);

        List<IngredientDto> result = ingredientService.getAllIngredients();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(ingredientRepository).findAll();
        verify(ingredientMapper).toDto(ingredient1);
        verify(ingredientMapper).toDto(ingredient2);
    }

    @Test
    public void testGetAllIngredientsEmpty() {
        when(ingredientRepository.findAll()).thenReturn(List.of());

        List<IngredientDto> result = ingredientService.getAllIngredients();

        assertTrue(result.isEmpty());
        verify(ingredientRepository).findAll();
    }

    // Tests pour saveIngredient
    @Test
    public void testSaveIngredientSuccess() {
        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Tomate");
        requestDto.setDescription("Tomate fraîche");
        requestDto.setPrix(1.5f);

        Ingredient entity = new Ingredient();
        entity.setNom("Tomate");
        entity.setDescription("Tomate fraîche");
        entity.setPrix(1.5f);

        Ingredient savedEntity = new Ingredient();
        savedEntity.setId(1L);
        savedEntity.setNom("Tomate");
        savedEntity.setDescription("Tomate fraîche");
        savedEntity.setPrix(1.5f);

        IngredientDto dto = new IngredientDto();
        dto.setId(1L);
        dto.setNom("Tomate");
        dto.setDescription("Tomate fraîche");
        dto.setPrix(1.5f);

        when(ingredientRepository.findByNom("Tomate")).thenReturn(null);
        when(ingredientMapper.toEntity(requestDto)).thenReturn(entity);
        when(ingredientRepository.save(entity)).thenReturn(savedEntity);
        when(ingredientMapper.toDto(savedEntity)).thenReturn(dto);

        IngredientDto result = ingredientService.saveIngredient(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Tomate", result.getNom());
        assertEquals("Tomate fraîche", result.getDescription());
        assertEquals(1.5f, result.getPrix());

        verify(ingredientRepository).findByNom("Tomate");
        verify(ingredientMapper).toEntity(requestDto);
        verify(ingredientRepository).save(entity);
        verify(ingredientMapper).toDto(savedEntity);
    }

    @Test
    public void testSaveIngredientNomAlreadyExists() {
        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Tomate");
        requestDto.setDescription("Tomate fraîche");
        requestDto.setPrix(1.5f);

        Ingredient existingIngredient = new Ingredient();
        existingIngredient.setId(1L);
        existingIngredient.setNom("Tomate");

        when(ingredientRepository.findByNom("Tomate")).thenReturn(existingIngredient);

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.saveIngredient(requestDto));
        assertEquals("L'ingrédient Tomate existe déjà", exception.getMessage());

        verify(ingredientRepository).findByNom("Tomate");
        verify(ingredientMapper, never()).toEntity(any());
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    public void testSaveIngredientNomNull() {
        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom(null);
        requestDto.setDescription("Description");
        requestDto.setPrix(1.5f);

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.saveIngredient(requestDto));
        assertTrue(exception.getMessage().contains("Le nom de l'ingrédient est obligatoire"));

        verify(ingredientRepository, never()).save(any());
    }

    @Test
    public void testSaveIngredientNomEmpty() {
        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("");
        requestDto.setDescription("Description");
        requestDto.setPrix(1.5f);

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.saveIngredient(requestDto));
        assertTrue(exception.getMessage().contains("Le nom de l'ingrédient est obligatoire"));

        verify(ingredientRepository, never()).save(any());
    }

    @Test
    public void testSaveIngredientDescriptionNull() {
        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Tomate");
        requestDto.setDescription(null);
        requestDto.setPrix(1.5f);

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.saveIngredient(requestDto));
        assertTrue(exception.getMessage().contains("La description de l'ingrédient est obligatoire"));

        verify(ingredientRepository, never()).save(any());
    }

    @Test
    public void testSaveIngredientDescriptionEmpty() {
        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Tomate");
        requestDto.setDescription("");
        requestDto.setPrix(1.5f);

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.saveIngredient(requestDto));
        assertTrue(exception.getMessage().contains("La description de l'ingrédient est obligatoire"));

        verify(ingredientRepository, never()).save(any());
    }

    @Test
    public void testSaveIngredientPrixNull() {
        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Tomate");
        requestDto.setDescription("Description");
        requestDto.setPrix(null);

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.saveIngredient(requestDto));
        assertTrue(exception.getMessage().contains("Le prix de l'ingrédient est obligatoire"));

        verify(ingredientRepository, never()).save(any());
    }

    @Test
    public void testSaveIngredientPrixZero() {
        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Tomate");
        requestDto.setDescription("Description");
        requestDto.setPrix(0f);

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.saveIngredient(requestDto));
        assertTrue(exception.getMessage().contains("Le prix de l'ingrédient est obligatoire et doit être supérieur à 0"));

        verify(ingredientRepository, never()).save(any());
    }

    @Test
    public void testSaveIngredientPrixNegative() {
        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Tomate");
        requestDto.setDescription("Description");
        requestDto.setPrix(-1.5f);

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.saveIngredient(requestDto));
        assertTrue(exception.getMessage().contains("Le prix de l'ingrédient est obligatoire et doit être supérieur à 0"));

        verify(ingredientRepository, never()).save(any());
    }

    @Test
    public void testSaveIngredientMultipleErrors() {
        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("");
        requestDto.setDescription("");
        requestDto.setPrix(0f);

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.saveIngredient(requestDto));
        assertTrue(exception.getMessage().contains("Le nom de l'ingrédient est obligatoire"));
        assertTrue(exception.getMessage().contains("La description de l'ingrédient est obligatoire"));
        assertTrue(exception.getMessage().contains("Le prix de l'ingrédient est obligatoire"));

        verify(ingredientRepository, never()).save(any());
    }

    // Tests pour deleteIngredientById
    @Test
    public void testDeleteIngredientByIdSuccess() {
        Long id = 1L;
        when(ingredientRepository.existsById(id)).thenReturn(true);
        doNothing().when(ingredientRepository).deleteById(id);
        doNothing().when(ingredientRepository).flush();

        ingredientService.deleteIngredientById(id);

        verify(ingredientRepository).existsById(id);
        verify(ingredientRepository).deleteById(id);
        verify(ingredientRepository).flush();
    }

    @Test
    public void testDeleteIngredientByIdNotFound() {
        Long id = 1L;
        when(ingredientRepository.existsById(id)).thenReturn(false);

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.deleteIngredientById(id));
        assertEquals("L'ingrédient n'existe pas", exception.getMessage());

        verify(ingredientRepository).existsById(id);
        verify(ingredientRepository, never()).deleteById(any());
    }

    @Test
    public void testDeleteIngredientByIdWithIntegrityViolation() {
        Long id = 1L;
        when(ingredientRepository.existsById(id)).thenReturn(true);
        doNothing().when(ingredientRepository).deleteById(id);
        doThrow(new DataIntegrityViolationException("FK constraint")).when(ingredientRepository).flush();

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.deleteIngredientById(id));
        assertEquals("L'ingrédient est présent dans une ou plusieurs pizzas", exception.getMessage());

        verify(ingredientRepository).existsById(id);
        verify(ingredientRepository).deleteById(id);
        verify(ingredientRepository).flush();
    }

    // Tests pour updateIngredient
    @Test
    public void testUpdateIngredientSuccess() {
        Long id = 1L;

        Ingredient oldIngredient = new Ingredient();
        oldIngredient.setId(id);
        oldIngredient.setNom("Tomate");
        oldIngredient.setDescription("Tomate fraîche");
        oldIngredient.setPrix(1.5f);

        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Tomate Bio");
        requestDto.setDescription("Tomate bio fraîche");
        requestDto.setPrix(2.0f);

        Ingredient newIngredient = new Ingredient();
        newIngredient.setNom("Tomate Bio");
        newIngredient.setDescription("Tomate bio fraîche");
        newIngredient.setPrix(2.0f);

        Ingredient savedIngredient = new Ingredient();
        savedIngredient.setId(id);
        savedIngredient.setNom("Tomate Bio");
        savedIngredient.setDescription("Tomate bio fraîche");
        savedIngredient.setPrix(2.0f);

        IngredientDto dto = new IngredientDto();
        dto.setId(id);
        dto.setNom("Tomate Bio");
        dto.setDescription("Tomate bio fraîche");
        dto.setPrix(2.0f);

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(oldIngredient));
        when(ingredientRepository.countByNom("Tomate Bio")).thenReturn(0L);
        when(ingredientMapper.toEntity(requestDto)).thenReturn(newIngredient);
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(savedIngredient);
        when(ingredientMapper.toDto(savedIngredient)).thenReturn(dto);

        IngredientDto result = ingredientService.updateIngredient(id, requestDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Tomate Bio", result.getNom());
        assertEquals("Tomate bio fraîche", result.getDescription());
        assertEquals(2.0f, result.getPrix());

        verify(ingredientRepository).findById(id);
        verify(ingredientRepository).countByNom("Tomate Bio");
        verify(ingredientMapper).toEntity(requestDto);
        verify(ingredientRepository).save(any(Ingredient.class));
        verify(ingredientMapper).toDto(savedIngredient);
    }

    @Test
    public void testUpdateIngredientNotFound() {
        Long id = 1L;
        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Tomate");
        requestDto.setDescription("Description");
        requestDto.setPrix(1.5f);

        when(ingredientRepository.findById(id)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> ingredientService.updateIngredient(id, requestDto));
        assertEquals("L'ingrédient n'existe pas", exception.getMessage());

        verify(ingredientRepository).findById(id);
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    public void testUpdateIngredientNomAlreadyExists() {
        Long id = 1L;

        Ingredient oldIngredient = new Ingredient();
        oldIngredient.setId(id);
        oldIngredient.setNom("Tomate");

        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Mozzarella");
        requestDto.setDescription("Description");
        requestDto.setPrix(1.5f);

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(oldIngredient));
        when(ingredientRepository.countByNom("Mozzarella")).thenReturn(1L);

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.updateIngredient(id, requestDto));
        assertEquals("L'ingrédient Mozzarella existe déjà", exception.getMessage());

        verify(ingredientRepository).findById(id);
        verify(ingredientRepository).countByNom("Mozzarella");
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    public void testUpdateIngredientSameNom() {
        Long id = 1L;

        Ingredient oldIngredient = new Ingredient();
        oldIngredient.setId(id);
        oldIngredient.setNom("Tomate");
        oldIngredient.setDescription("Tomate fraîche");
        oldIngredient.setPrix(1.5f);

        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Tomate");
        requestDto.setDescription("Tomate bio fraîche");
        requestDto.setPrix(2.0f);

        Ingredient newIngredient = new Ingredient();
        newIngredient.setNom("Tomate");
        newIngredient.setDescription("Tomate bio fraîche");
        newIngredient.setPrix(2.0f);

        Ingredient savedIngredient = new Ingredient();
        savedIngredient.setId(id);
        savedIngredient.setNom("Tomate");
        savedIngredient.setDescription("Tomate bio fraîche");
        savedIngredient.setPrix(2.0f);

        IngredientDto dto = new IngredientDto();
        dto.setId(id);
        dto.setNom("Tomate");
        dto.setDescription("Tomate bio fraîche");
        dto.setPrix(2.0f);

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(oldIngredient));
        when(ingredientMapper.toEntity(requestDto)).thenReturn(newIngredient);
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(savedIngredient);
        when(ingredientMapper.toDto(savedIngredient)).thenReturn(dto);

        IngredientDto result = ingredientService.updateIngredient(id, requestDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Tomate", result.getNom());
        assertEquals("Tomate bio fraîche", result.getDescription());
        assertEquals(2.0f, result.getPrix());

        verify(ingredientRepository).findById(id);
        verify(ingredientMapper).toEntity(requestDto);
        verify(ingredientRepository).save(any(Ingredient.class));
        verify(ingredientMapper).toDto(savedIngredient);
    }

    @Test
    public void testUpdateIngredientNomNull() {
        Long id = 1L;

        Ingredient oldIngredient = new Ingredient();
        oldIngredient.setId(id);
        oldIngredient.setNom("Tomate");

        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom(null);
        requestDto.setDescription("Description");
        requestDto.setPrix(1.5f);

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(oldIngredient));

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.updateIngredient(id, requestDto));
        assertTrue(exception.getMessage().contains("Le nom de l'ingrédient est obligatoire"));

        verify(ingredientRepository).findById(id);
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    public void testUpdateIngredientDescriptionNull() {
        Long id = 1L;

        Ingredient oldIngredient = new Ingredient();
        oldIngredient.setId(id);
        oldIngredient.setNom("Tomate");

        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Tomate");
        requestDto.setDescription(null);
        requestDto.setPrix(1.5f);

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(oldIngredient));

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.updateIngredient(id, requestDto));
        assertTrue(exception.getMessage().contains("La description de l'ingrédient est obligatoire"));

        verify(ingredientRepository).findById(id);
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    public void testUpdateIngredientPrixNull() {
        Long id = 1L;

        Ingredient oldIngredient = new Ingredient();
        oldIngredient.setId(id);
        oldIngredient.setNom("Tomate");

        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Tomate");
        requestDto.setDescription("Description");
        requestDto.setPrix(null);

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(oldIngredient));

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.updateIngredient(id, requestDto));
        assertTrue(exception.getMessage().contains("Le prix de l'ingrédient est obligatoire"));

        verify(ingredientRepository).findById(id);
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    public void testUpdateIngredientPrixZero() {
        Long id = 1L;

        Ingredient oldIngredient = new Ingredient();
        oldIngredient.setId(id);
        oldIngredient.setNom("Tomate");

        IngredientRequestDto requestDto = new IngredientRequestDto();
        requestDto.setNom("Tomate");
        requestDto.setDescription("Description");
        requestDto.setPrix(0f);

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(oldIngredient));

        Conflict exception = assertThrows(Conflict.class, () -> ingredientService.updateIngredient(id, requestDto));
        assertTrue(exception.getMessage().contains("Le prix de l'ingrédient est obligatoire et doit être supérieur à 0"));

        verify(ingredientRepository).findById(id);
        verify(ingredientRepository, never()).save(any());
    }
}