package com.projetm1.pizzeria.pizzaPanier;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.projetm1.pizzeria.error.NotFound;
import com.projetm1.pizzeria.ingredient.Ingredient;
import com.projetm1.pizzeria.ingredient.IngredientRepository;
import com.projetm1.pizzeria.ingredient.dto.IngredientDto;
import com.projetm1.pizzeria.pizza.Pizza;
import com.projetm1.pizzeria.pizza.PizzaRepository;
import com.projetm1.pizzeria.pizza.dto.PizzaLightDto;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierDto;
import com.projetm1.pizzeria.pizzaPanier.dto.PizzaPanierRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class PizzaPanierServiceTest {

    @InjectMocks
    private PizzaPanierService pizzaPanierService;

    @Mock
    private PizzaPanierRepository pizzaPanierRepository;

    @Mock
    private PizzaPanierMapper pizzaPanierMapper;

    @Mock
    private PizzaRepository pizzaRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    // Tests pour getPizzaPanierById
    @Test
    public void testGetPizzaPanierByIdFound() {
        Long id = 1L;
        PizzaPanier pizzaPanier = new PizzaPanier();
        pizzaPanier.setId(id);
        PizzaPanierDto dto = new PizzaPanierDto();
        dto.setId(id);

        when(pizzaPanierRepository.findById(id)).thenReturn(Optional.of(pizzaPanier));
        when(pizzaPanierMapper.toDto(pizzaPanier)).thenReturn(dto);

        PizzaPanierDto result = pizzaPanierService.getPizzaPanierById(id);

        assertEquals(id, result.getId());
        verify(pizzaPanierRepository).findById(id);
        verify(pizzaPanierMapper).toDto(pizzaPanier);
    }

    @Test
    public void testGetPizzaPanierByIdNotFound() {
        Long id = 1L;
        when(pizzaPanierRepository.findById(id)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> pizzaPanierService.getPizzaPanierById(id));
        assertEquals("PizzaPanier not found", exception.getMessage());
        verify(pizzaPanierRepository).findById(id);
    }

    // Tests pour getAllPizzasPaniers
    @Test
    public void testGetAllPizzasPaniersWithData() {
        PizzaPanier pizzaPanier1 = new PizzaPanier();
        pizzaPanier1.setId(1L);
        PizzaPanier pizzaPanier2 = new PizzaPanier();
        pizzaPanier2.setId(2L);

        PizzaPanierDto dto1 = new PizzaPanierDto();
        dto1.setId(1L);
        PizzaPanierDto dto2 = new PizzaPanierDto();
        dto2.setId(2L);

        List<PizzaPanier> pizzaPaniers = Arrays.asList(pizzaPanier1, pizzaPanier2);

        when(pizzaPanierRepository.findAll()).thenReturn(pizzaPaniers);
        when(pizzaPanierMapper.toDto(pizzaPanier1)).thenReturn(dto1);
        when(pizzaPanierMapper.toDto(pizzaPanier2)).thenReturn(dto2);

        List<PizzaPanierDto> result = pizzaPanierService.getAllPizzasPaniers();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(pizzaPanierRepository).findAll();
        verify(pizzaPanierMapper).toDto(pizzaPanier1);
        verify(pizzaPanierMapper).toDto(pizzaPanier2);
    }

    @Test
    public void testGetAllPizzasPaniersEmpty() {
        when(pizzaPanierRepository.findAll()).thenReturn(List.of());

        List<PizzaPanierDto> result = pizzaPanierService.getAllPizzasPaniers();

        assertTrue(result.isEmpty());
        verify(pizzaPanierRepository).findAll();
    }

    // Tests pour savePizzaPanier
    @Test
    public void testSavePizzaPanierSuccess() {
        Long pizzaId = 1L;
        Set<Long> ingredientIds = new HashSet<>(Arrays.asList(1L, 2L));

        PizzaPanierRequestDto requestDto = new PizzaPanierRequestDto();
        requestDto.setPizzaId(pizzaId);
        requestDto.setIngredientsIds(ingredientIds);

        Pizza pizza = new Pizza();
        pizza.setId(pizzaId);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);

        PizzaPanier pizzaPanierToSave = new PizzaPanier();

        PizzaPanier savedPizzaPanier = new PizzaPanier();
        savedPizzaPanier.setId(1L);
        savedPizzaPanier.setPizza(pizza);
        savedPizzaPanier.setIngredients(Arrays.asList(ingredient1, ingredient2));

        PizzaPanierDto expectedDto = new PizzaPanierDto();
        expectedDto.setId(1L);
        PizzaLightDto pizzaLightDto = new PizzaLightDto();
        pizzaLightDto.setId(pizzaId);
        expectedDto.setPizza(pizzaLightDto);
        List<IngredientDto> ingredientDtos = new ArrayList<>();
        IngredientDto ingredientDto1 = new IngredientDto();
        ingredientDto1.setId(1L);
        IngredientDto ingredientDto2 = new IngredientDto();
        ingredientDto2.setId(2L);
        ingredientDtos.add(ingredientDto1);
        ingredientDtos.add(ingredientDto2);
        expectedDto.setIngredients(ingredientDtos);

        when(pizzaPanierMapper.toEntity(requestDto)).thenReturn(pizzaPanierToSave);
        when(pizzaRepository.findById(pizzaId)).thenReturn(Optional.of(pizza));
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient1));
        when(ingredientRepository.findById(2L)).thenReturn(Optional.of(ingredient2));
        when(pizzaPanierRepository.save(any(PizzaPanier.class))).thenReturn(savedPizzaPanier);
        when(pizzaPanierMapper.toDto(savedPizzaPanier)).thenReturn(expectedDto);

        PizzaPanierDto result = pizzaPanierService.savePizzaPanier(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(pizzaId, result.getPizza().getId());
        assertEquals(2, result.getIngredients().size());

        verify(pizzaPanierMapper).toEntity(requestDto);
        verify(pizzaRepository).findById(pizzaId);
        verify(ingredientRepository).findById(1L);
        verify(ingredientRepository).findById(2L);
        verify(pizzaPanierRepository).save(any(PizzaPanier.class));
        verify(pizzaPanierMapper).toDto(savedPizzaPanier);
    }

    @Test
    public void testSavePizzaPanierPizzaNotFound() {
        Long pizzaId = 1L;
        Set<Long> ingredientIds = new HashSet<>(Arrays.asList(1L, 2L));

        PizzaPanierRequestDto requestDto = new PizzaPanierRequestDto();
        requestDto.setPizzaId(pizzaId);
        requestDto.setIngredientsIds(ingredientIds);

        PizzaPanier pizzaPanierToSave = new PizzaPanier();

        when(pizzaPanierMapper.toEntity(requestDto)).thenReturn(pizzaPanierToSave);
        when(pizzaRepository.findById(pizzaId)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> pizzaPanierService.savePizzaPanier(requestDto));
        assertEquals("Pizza not found", exception.getMessage());

        verify(pizzaPanierMapper).toEntity(requestDto);
        verify(pizzaRepository).findById(pizzaId);
        verify(pizzaPanierRepository, never()).save(any(PizzaPanier.class));
    }

    @Test
    public void testSavePizzaPanierWithNonExistentIngredients() {
        Long pizzaId = 1L;
        Set<Long> ingredientIds = new HashSet<>(Arrays.asList(1L, 2L));

        PizzaPanierRequestDto requestDto = new PizzaPanierRequestDto();
        requestDto.setPizzaId(pizzaId);
        requestDto.setIngredientsIds(ingredientIds);

        Pizza pizza = new Pizza();
        pizza.setId(pizzaId);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);

        PizzaPanier pizzaPanierToSave = new PizzaPanier();

        PizzaPanier savedPizzaPanier = new PizzaPanier();
        savedPizzaPanier.setId(1L);
        savedPizzaPanier.setPizza(pizza);
        savedPizzaPanier.setIngredients(List.of(ingredient1));

        PizzaPanierDto expectedDto = new PizzaPanierDto();
        expectedDto.setId(1L);
        PizzaLightDto pizzaLightDto = new PizzaLightDto();
        pizzaLightDto.setId(pizzaId);
        expectedDto.setPizza(pizzaLightDto);
        List<IngredientDto> ingredientDtos = new ArrayList<>();
        IngredientDto ingredientDto1 = new IngredientDto();
        ingredientDto1.setId(1L);
        ingredientDtos.add(ingredientDto1);
        expectedDto.setIngredients(ingredientDtos);

        when(pizzaPanierMapper.toEntity(requestDto)).thenReturn(pizzaPanierToSave);
        when(pizzaRepository.findById(pizzaId)).thenReturn(Optional.of(pizza));
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient1));
        when(ingredientRepository.findById(2L)).thenReturn(Optional.empty());
        when(pizzaPanierRepository.save(any(PizzaPanier.class))).thenReturn(savedPizzaPanier);
        when(pizzaPanierMapper.toDto(savedPizzaPanier)).thenReturn(expectedDto);

        PizzaPanierDto result = pizzaPanierService.savePizzaPanier(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(pizzaId, result.getPizza().getId());
        assertEquals(1, result.getIngredients().size());

        verify(pizzaPanierMapper).toEntity(requestDto);
        verify(pizzaRepository).findById(pizzaId);
        verify(ingredientRepository).findById(1L);
        verify(ingredientRepository).findById(2L);
        verify(pizzaPanierRepository).save(any(PizzaPanier.class));
        verify(pizzaPanierMapper).toDto(savedPizzaPanier);
    }

    // Tests pour deletePizzaPanierById
    @Test
    public void testDeletePizzaPanierByIdSuccess() {
        Long id = 1L;
        doNothing().when(pizzaPanierRepository).deleteById(id);

        pizzaPanierService.deletePizzaPanierById(id);

        verify(pizzaPanierRepository).deleteById(id);
    }

    // Tests pour updatePizzaPanier
    @Test
    public void testUpdatePizzaPanierSuccess() {
        Long id = 1L;
        Long pizzaId = 2L;
        Set<Long> ingredientIds = new HashSet<>(Arrays.asList(1L, 2L));

        PizzaPanierRequestDto requestDto = new PizzaPanierRequestDto();
        requestDto.setPizzaId(pizzaId);
        requestDto.setIngredientsIds(ingredientIds);

        Pizza pizza = new Pizza();
        pizza.setId(pizzaId);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);

        PizzaPanier pizzaPanierToUpdate = new PizzaPanier();

        PizzaPanier updatedPizzaPanier = new PizzaPanier();
        updatedPizzaPanier.setId(id);
        updatedPizzaPanier.setPizza(pizza);
        updatedPizzaPanier.setIngredients(Arrays.asList(ingredient1, ingredient2));

        PizzaPanierDto expectedDto = new PizzaPanierDto();
        expectedDto.setId(id);
        PizzaLightDto pizzaLightDto = new PizzaLightDto();
        pizzaLightDto.setId(pizzaId);
        expectedDto.setPizza(pizzaLightDto);
        List<IngredientDto> ingredientDtos = new ArrayList<>();
        IngredientDto ingredientDto1 = new IngredientDto();
        ingredientDto1.setId(1L);
        IngredientDto ingredientDto2 = new IngredientDto();
        ingredientDto2.setId(2L);
        ingredientDtos.add(ingredientDto1);
        ingredientDtos.add(ingredientDto2);
        expectedDto.setIngredients(ingredientDtos);

        when(pizzaPanierRepository.existsById(id)).thenReturn(true);
        when(pizzaPanierMapper.toEntity(requestDto)).thenReturn(pizzaPanierToUpdate);
        when(pizzaRepository.findById(pizzaId)).thenReturn(Optional.of(pizza));
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient1));
        when(ingredientRepository.findById(2L)).thenReturn(Optional.of(ingredient2));
        when(pizzaPanierRepository.save(any(PizzaPanier.class))).thenReturn(updatedPizzaPanier);
        when(pizzaPanierMapper.toDto(updatedPizzaPanier)).thenReturn(expectedDto);

        PizzaPanierDto result = pizzaPanierService.updatePizzaPanier(id, requestDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(pizzaId, result.getPizza().getId());
        assertEquals(2, result.getIngredients().size());

        verify(pizzaPanierRepository).existsById(id);
        verify(pizzaPanierMapper).toEntity(requestDto);
        verify(pizzaRepository).findById(pizzaId);
        verify(ingredientRepository).findById(1L);
        verify(ingredientRepository).findById(2L);
        verify(pizzaPanierRepository).save(any(PizzaPanier.class));
        verify(pizzaPanierMapper).toDto(updatedPizzaPanier);
    }

    @Test
    public void testUpdatePizzaPanierNotFound() {
        Long id = 1L;
        PizzaPanierRequestDto requestDto = new PizzaPanierRequestDto();
        requestDto.setPizzaId(2L);
        requestDto.setIngredientsIds(Set.of(1L, 2L));

        when(pizzaPanierRepository.existsById(id)).thenReturn(false);

        NotFound exception = assertThrows(NotFound.class, () -> pizzaPanierService.updatePizzaPanier(id, requestDto));
        assertEquals("PizzaPanier not found", exception.getMessage());

        verify(pizzaPanierRepository).existsById(id);
        verify(pizzaPanierRepository, never()).save(any(PizzaPanier.class));
    }

    @Test
    public void testUpdatePizzaPanierWithNonExistentPizza() {
        Long id = 1L;
        Long pizzaId = 2L;
        Set<Long> ingredientIds = new HashSet<>(Arrays.asList(1L, 2L));

        PizzaPanierRequestDto requestDto = new PizzaPanierRequestDto();
        requestDto.setPizzaId(pizzaId);
        requestDto.setIngredientsIds(ingredientIds);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);

        PizzaPanier pizzaPanierToUpdate = new PizzaPanier();

        PizzaPanier updatedPizzaPanier = new PizzaPanier();
        updatedPizzaPanier.setId(id);
        updatedPizzaPanier.setPizza(null);
        updatedPizzaPanier.setIngredients(Arrays.asList(ingredient1, ingredient2));

        PizzaPanierDto expectedDto = new PizzaPanierDto();
        expectedDto.setId(id);
        expectedDto.setPizza(null);
        List<IngredientDto> ingredientDtos = new ArrayList<>();
        IngredientDto ingredientDto1 = new IngredientDto();
        ingredientDto1.setId(1L);
        IngredientDto ingredientDto2 = new IngredientDto();
        ingredientDto2.setId(2L);
        ingredientDtos.add(ingredientDto1);
        ingredientDtos.add(ingredientDto2);
        expectedDto.setIngredients(ingredientDtos);

        when(pizzaPanierRepository.existsById(id)).thenReturn(true);
        when(pizzaPanierMapper.toEntity(requestDto)).thenReturn(pizzaPanierToUpdate);
        when(pizzaRepository.findById(pizzaId)).thenReturn(Optional.empty());
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient1));
        when(ingredientRepository.findById(2L)).thenReturn(Optional.of(ingredient2));
        when(pizzaPanierRepository.save(any(PizzaPanier.class))).thenReturn(updatedPizzaPanier);
        when(pizzaPanierMapper.toDto(updatedPizzaPanier)).thenReturn(expectedDto);

        PizzaPanierDto result = pizzaPanierService.updatePizzaPanier(id, requestDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertNull(result.getPizza());
        assertEquals(2, result.getIngredients().size());

        verify(pizzaPanierRepository).existsById(id);
        verify(pizzaPanierMapper).toEntity(requestDto);
        verify(pizzaRepository).findById(pizzaId);
        verify(ingredientRepository).findById(1L);
        verify(ingredientRepository).findById(2L);
        verify(pizzaPanierRepository).save(any(PizzaPanier.class));
        verify(pizzaPanierMapper).toDto(updatedPizzaPanier);
    }

    @Test
    public void testUpdatePizzaPanierWithNonExistentIngredients() {
        Long id = 1L;
        Long pizzaId = 2L;
        Set<Long> ingredientIds = new HashSet<>(Arrays.asList(1L, 2L));

        PizzaPanierRequestDto requestDto = new PizzaPanierRequestDto();
        requestDto.setPizzaId(pizzaId);
        requestDto.setIngredientsIds(ingredientIds);

        Pizza pizza = new Pizza();
        pizza.setId(pizzaId);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);

        PizzaPanier pizzaPanierToUpdate = new PizzaPanier();

        PizzaPanier updatedPizzaPanier = new PizzaPanier();
        updatedPizzaPanier.setId(id);
        updatedPizzaPanier.setPizza(pizza);
        updatedPizzaPanier.setIngredients(List.of(ingredient1));

        PizzaPanierDto expectedDto = new PizzaPanierDto();
        expectedDto.setId(id);
        PizzaLightDto pizzaLightDto = new PizzaLightDto();
        pizzaLightDto.setId(pizzaId);
        expectedDto.setPizza(pizzaLightDto);
        List<IngredientDto> ingredientDtos = new ArrayList<>();
        IngredientDto ingredientDto1 = new IngredientDto();
        ingredientDto1.setId(1L);
        ingredientDtos.add(ingredientDto1);
        expectedDto.setIngredients(ingredientDtos);

        when(pizzaPanierRepository.existsById(id)).thenReturn(true);
        when(pizzaPanierMapper.toEntity(requestDto)).thenReturn(pizzaPanierToUpdate);
        when(pizzaRepository.findById(pizzaId)).thenReturn(Optional.of(pizza));
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient1));
        when(ingredientRepository.findById(2L)).thenReturn(Optional.empty());
        when(pizzaPanierRepository.save(any(PizzaPanier.class))).thenReturn(updatedPizzaPanier);
        when(pizzaPanierMapper.toDto(updatedPizzaPanier)).thenReturn(expectedDto);

        PizzaPanierDto result = pizzaPanierService.updatePizzaPanier(id, requestDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(pizzaId, result.getPizza().getId());
        assertEquals(1, result.getIngredients().size());

        verify(pizzaPanierRepository).existsById(id);
        verify(pizzaPanierMapper).toEntity(requestDto);
        verify(pizzaRepository).findById(pizzaId);
        verify(ingredientRepository).findById(1L);
        verify(ingredientRepository).findById(2L);
        verify(pizzaPanierRepository).save(any(PizzaPanier.class));
        verify(pizzaPanierMapper).toDto(updatedPizzaPanier);
    }

    @Test
    public void testUpdatePizzaPanierWithEmptyIngredientsList() {
        Long id = 1L;
        Long pizzaId = 2L;
        Set<Long> ingredientIds = new HashSet<>();

        PizzaPanierRequestDto requestDto = new PizzaPanierRequestDto();
        requestDto.setPizzaId(pizzaId);
        requestDto.setIngredientsIds(ingredientIds);

        Pizza pizza = new Pizza();
        pizza.setId(pizzaId);

        PizzaPanier pizzaPanierToUpdate = new PizzaPanier();

        PizzaPanier updatedPizzaPanier = new PizzaPanier();
        updatedPizzaPanier.setId(id);
        updatedPizzaPanier.setPizza(pizza);
        updatedPizzaPanier.setIngredients(List.of());

        PizzaPanierDto expectedDto = new PizzaPanierDto();
        expectedDto.setId(id);
        PizzaLightDto pizzaLightDto = new PizzaLightDto();
        pizzaLightDto.setId(pizzaId);
        expectedDto.setPizza(pizzaLightDto);
        expectedDto.setIngredients(List.of());

        when(pizzaPanierRepository.existsById(id)).thenReturn(true);
        when(pizzaPanierMapper.toEntity(requestDto)).thenReturn(pizzaPanierToUpdate);
        when(pizzaRepository.findById(pizzaId)).thenReturn(Optional.of(pizza));
        when(pizzaPanierRepository.save(any(PizzaPanier.class))).thenReturn(updatedPizzaPanier);
        when(pizzaPanierMapper.toDto(updatedPizzaPanier)).thenReturn(expectedDto);

        PizzaPanierDto result = pizzaPanierService.updatePizzaPanier(id, requestDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(pizzaId, result.getPizza().getId());
        assertTrue(result.getIngredients().isEmpty());

        verify(pizzaPanierRepository).existsById(id);
        verify(pizzaPanierMapper).toEntity(requestDto);
        verify(pizzaRepository).findById(pizzaId);
        verify(pizzaPanierRepository).save(any(PizzaPanier.class));
        verify(pizzaPanierMapper).toDto(updatedPizzaPanier);
    }
}