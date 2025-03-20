package com.projetm1.pizzeria.pizza;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.projetm1.pizzeria.error.NotFound;
import com.projetm1.pizzeria.error.UnprocessableEntity;
import com.projetm1.pizzeria.image.ImageService;
import com.projetm1.pizzeria.pizza.dto.PizzaDto;
import com.projetm1.pizzeria.pizza.dto.PizzaRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

class PizzaServiceTest {
    @InjectMocks
    private PizzaService pizzaService;

    @Mock
    private PizzaRepository pizzaRepository;

    @Mock
    private PizzaMapper pizzaMapper;

    @Mock
    private ImageService imageService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    // Tests pour getPizzaById
    @Test
    public void testGetPizzaByIdFound() {
        Long id = 1L;
        Pizza pizza = new Pizza();
        pizza.setId(id);
        PizzaDto dto = new PizzaDto();
        dto.setId(id);

        when(pizzaRepository.existsById(id)).thenReturn(true);
        when(pizzaRepository.findByIdWithIngredients(id)).thenReturn(Optional.of(pizza));
        when(pizzaMapper.toDto(pizza)).thenReturn(dto);

        PizzaDto result = pizzaService.getPizzaById(id);

        assertEquals(id, result.getId());
        verify(pizzaRepository).existsById(id);
        verify(pizzaRepository).findByIdWithIngredients(id);
        verify(pizzaMapper).toDto(pizza);
    }

    @Test
    public void testGetPizzaByIdNotFound() {
        Long id = 1L;
        when(pizzaRepository.existsById(id)).thenReturn(false);

        NotFound exception = assertThrows(NotFound.class, () -> pizzaService.getPizzaById(id));
        assertEquals("La pizza n'existe pas", exception.getMessage());
        verify(pizzaRepository).existsById(id);
        verify(pizzaRepository, never()).findByIdWithIngredients(any());
    }

    @Test
    public void testGetPizzaByIdRepositoryReturnEmpty() {
        Long id = 1L;
        when(pizzaRepository.existsById(id)).thenReturn(true);
        when(pizzaRepository.findByIdWithIngredients(id)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> pizzaService.getPizzaById(id));
        assertEquals("La pizza n'existe pas", exception.getMessage());
        verify(pizzaRepository).existsById(id);
        verify(pizzaRepository).findByIdWithIngredients(id);
    }

    // Tests pour getAllPizzas
    @Test
    public void testGetAllPizzasWithData() {
        Pizza pizza1 = new Pizza();
        pizza1.setId(1L);
        Pizza pizza2 = new Pizza();
        pizza2.setId(2L);

        PizzaDto dto1 = new PizzaDto();
        dto1.setId(1L);
        PizzaDto dto2 = new PizzaDto();
        dto2.setId(2L);

        List<Pizza> pizzas = Arrays.asList(pizza1, pizza2);

        when(pizzaRepository.findAllWithIngredients()).thenReturn(pizzas);
        when(pizzaMapper.toDto(pizza1)).thenReturn(dto1);
        when(pizzaMapper.toDto(pizza2)).thenReturn(dto2);

        List<PizzaDto> result = pizzaService.getAllPizzas();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(pizzaRepository).findAllWithIngredients();
        verify(pizzaMapper).toDto(pizza1);
        verify(pizzaMapper).toDto(pizza2);
    }

    @Test
    public void testGetAllPizzasEmpty() {
        when(pizzaRepository.findAllWithIngredients()).thenReturn(List.of());

        List<PizzaDto> result = pizzaService.getAllPizzas();

        assertTrue(result.isEmpty());
        verify(pizzaRepository).findAllWithIngredients();
    }

    // Tests pour savePizza
    @Test
    public void testSavePizzaSuccess() {
        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("Margherita");
        requestDto.setDescription("Pizza classique");
        Set<Long> standardIngredients = new HashSet<>(Arrays.asList(1L, 2L));
        requestDto.setStandardIngredientsIds(standardIngredients);
        Set<Long> optionalIngredients = new HashSet<>(Arrays.asList(3L, 4L));
        requestDto.setOptionalIngredientsIds(optionalIngredients);
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        String fileName = "photo.jpg";
        when(imageService.saveImage(photoFile)).thenReturn(fileName);

        Pizza entity = new Pizza();
        entity.setNom("Margherita");
        entity.setDescription("Pizza classique");

        Pizza savedEntity = new Pizza();
        savedEntity.setId(1L);
        savedEntity.setNom("Margherita");
        savedEntity.setDescription("Pizza classique");
        savedEntity.setPhoto(fileName);

        PizzaDto dto = new PizzaDto();
        dto.setId(1L);
        dto.setNom("Margherita");
        dto.setDescription("Pizza classique");
        dto.setPhoto(fileName);

        when(pizzaMapper.toEntity(requestDto)).thenReturn(entity);
        when(pizzaRepository.save(any(Pizza.class))).thenReturn(savedEntity);
        when(pizzaMapper.toDto(savedEntity)).thenReturn(dto);

        PizzaDto result = pizzaService.savePizza(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Margherita", result.getNom());
        assertEquals("Pizza classique", result.getDescription());
        assertEquals(fileName, result.getPhoto());

        verify(imageService).saveImage(photoFile);
        verify(pizzaMapper).toEntity(requestDto);
        verify(pizzaRepository).save(any(Pizza.class));
        verify(pizzaMapper).toDto(savedEntity);
    }

    @Test
    public void testSavePizzaNomNull() {
        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom(null);
        requestDto.setDescription("Pizza classique");
        Set<Long> standardIngredients = new HashSet<>(Arrays.asList(1L, 2L));
        requestDto.setStandardIngredientsIds(standardIngredients);
        Set<Long> optionalIngredients = new HashSet<>(Arrays.asList(3L, 4L));
        requestDto.setOptionalIngredientsIds(optionalIngredients);
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        String fileName = "photo.jpg";
        when(imageService.saveImage(photoFile)).thenReturn(fileName);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> pizzaService.savePizza(requestDto));
        assertTrue(exception.getMessage().contains("Le nom de la pizza est obligatoire"));
        verify(imageService).saveImage(photoFile);
        verify(pizzaRepository, never()).save(any());
    }

    @Test
    public void testSavePizzaNomEmpty() {
        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("");
        requestDto.setDescription("Pizza classique");
        Set<Long> standardIngredients = new HashSet<>(Arrays.asList(1L, 2L));
        requestDto.setStandardIngredientsIds(standardIngredients);
        Set<Long> optionalIngredients = new HashSet<>(Arrays.asList(3L, 4L));
        requestDto.setOptionalIngredientsIds(optionalIngredients);
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        String fileName = "photo.jpg";
        when(imageService.saveImage(photoFile)).thenReturn(fileName);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> pizzaService.savePizza(requestDto));
        assertTrue(exception.getMessage().contains("Le nom de la pizza est obligatoire"));
        verify(imageService).saveImage(photoFile);
        verify(pizzaRepository, never()).save(any());
    }

    @Test
    public void testSavePizzaDescriptionNull() {
        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("Margherita");
        requestDto.setDescription(null);
        Set<Long> standardIngredients = new HashSet<>(Arrays.asList(1L, 2L));
        requestDto.setStandardIngredientsIds(standardIngredients);
        Set<Long> optionalIngredients = new HashSet<>(Arrays.asList(3L, 4L));
        requestDto.setOptionalIngredientsIds(optionalIngredients);
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        String fileName = "photo.jpg";
        when(imageService.saveImage(photoFile)).thenReturn(fileName);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> pizzaService.savePizza(requestDto));
        assertTrue(exception.getMessage().contains("La description de la pizza est obligatoire"));
        verify(imageService).saveImage(photoFile);
        verify(pizzaRepository, never()).save(any());
    }

    @Test
    public void testSavePizzaDescriptionEmpty() {
        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("Margherita");
        requestDto.setDescription("");
        Set<Long> standardIngredients = new HashSet<>(Arrays.asList(1L, 2L));
        requestDto.setStandardIngredientsIds(standardIngredients);
        Set<Long> optionalIngredients = new HashSet<>(Arrays.asList(3L, 4L));
        requestDto.setOptionalIngredientsIds(optionalIngredients);
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        String fileName = "photo.jpg";
        when(imageService.saveImage(photoFile)).thenReturn(fileName);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> pizzaService.savePizza(requestDto));
        assertTrue(exception.getMessage().contains("La description de la pizza est obligatoire"));
        verify(imageService).saveImage(photoFile);
        verify(pizzaRepository, never()).save(any());
    }

    @Test
    public void testSavePizzaStandardIngredientsNull() {
        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("Margherita");
        requestDto.setDescription("Pizza classique");
        requestDto.setStandardIngredientsIds(null);
        Set<Long> optionalIngredients = new HashSet<>(Arrays.asList(3L, 4L));
        requestDto.setOptionalIngredientsIds(optionalIngredients);
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        String fileName = "photo.jpg";
        when(imageService.saveImage(photoFile)).thenReturn(fileName);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> pizzaService.savePizza(requestDto));
        assertTrue(exception.getMessage().contains("Les ingrédients de base de la pizza sont obligatoires"));
        verify(imageService).saveImage(photoFile);
        verify(pizzaRepository, never()).save(any());
    }

    @Test
    public void testSavePizzaStandardIngredientsEmpty() {
        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("Margherita");
        requestDto.setDescription("Pizza classique");
        requestDto.setStandardIngredientsIds(new HashSet<>());
        Set<Long> optionalIngredients = new HashSet<>(Arrays.asList(3L, 4L));
        requestDto.setOptionalIngredientsIds(optionalIngredients);
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        String fileName = "photo.jpg";
        when(imageService.saveImage(photoFile)).thenReturn(fileName);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> pizzaService.savePizza(requestDto));
        assertTrue(exception.getMessage().contains("Les ingrédients de base de la pizza sont obligatoires"));
        verify(imageService).saveImage(photoFile);
        verify(pizzaRepository, never()).save(any());
    }

    @Test
    public void testSavePizzaOptionalIngredientsNull() {
        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("Margherita");
        requestDto.setDescription("Pizza classique");
        Set<Long> standardIngredients = new HashSet<>(Arrays.asList(1L, 2L));
        requestDto.setStandardIngredientsIds(standardIngredients);
        requestDto.setOptionalIngredientsIds(null);
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        String fileName = "photo.jpg";
        when(imageService.saveImage(photoFile)).thenReturn(fileName);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> pizzaService.savePizza(requestDto));
        assertTrue(exception.getMessage().contains("Les ingrédients optionnels de la pizza sont obligatoires"));
        verify(imageService).saveImage(photoFile);
        verify(pizzaRepository, never()).save(any());
    }

    @Test
    public void testSavePizzaOptionalIngredientsEmpty() {
        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("Margherita");
        requestDto.setDescription("Pizza classique");
        Set<Long> standardIngredients = new HashSet<>(Arrays.asList(1L, 2L));
        requestDto.setStandardIngredientsIds(standardIngredients);
        requestDto.setOptionalIngredientsIds(new HashSet<>());
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        String fileName = "photo.jpg";
        when(imageService.saveImage(photoFile)).thenReturn(fileName);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> pizzaService.savePizza(requestDto));
        assertTrue(exception.getMessage().contains("Les ingrédients optionnels de la pizza sont obligatoires"));
        verify(imageService).saveImage(photoFile);
        verify(pizzaRepository, never()).save(any());
    }

    @Test
    public void testSavePizzaPhotoNull() {
        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("Margherita");
        requestDto.setDescription("Pizza classique");
        Set<Long> standardIngredients = new HashSet<>(Arrays.asList(1L, 2L));
        requestDto.setStandardIngredientsIds(standardIngredients);
        Set<Long> optionalIngredients = new HashSet<>(Arrays.asList(3L, 4L));
        requestDto.setOptionalIngredientsIds(optionalIngredients);
        requestDto.setPhoto(null);

        when(imageService.saveImage(null)).thenReturn(null);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> pizzaService.savePizza(requestDto));
        assertTrue(exception.getMessage().contains("La photo de la pizza est obligatoire"));
        verify(imageService).saveImage(null);
        verify(pizzaRepository, never()).save(any());
    }

    @Test
    public void testSavePizzaMultipleErrors() {
        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("");
        requestDto.setDescription("");
        requestDto.setStandardIngredientsIds(null);
        requestDto.setOptionalIngredientsIds(null);
        requestDto.setPhoto(null);

        when(imageService.saveImage(null)).thenReturn(null);

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () -> pizzaService.savePizza(requestDto));
        String message = exception.getMessage();
        assertTrue(message.contains("Le nom de la pizza est obligatoire"));
        assertTrue(message.contains("La description de la pizza est obligatoire"));
        assertTrue(message.contains("Les ingrédients de base de la pizza sont obligatoires"));
        assertTrue(message.contains("Les ingrédients optionnels de la pizza sont obligatoires"));
        assertTrue(message.contains("La photo de la pizza est obligatoire"));
        verify(imageService).saveImage(null);
        verify(pizzaRepository, never()).save(any());
    }

    // Tests pour deletePizzaById
    @Test
    public void testDeletePizzaByIdSuccess() {
        Long id = 1L;
        when(pizzaRepository.existsById(id)).thenReturn(true);
        doNothing().when(pizzaRepository).deleteById(id);

        pizzaService.deletePizzaById(id);

        verify(pizzaRepository).existsById(id);
        verify(pizzaRepository).deleteById(id);
    }

    @Test
    public void testDeletePizzaByIdNotFound() {
        Long id = 1L;
        when(pizzaRepository.existsById(id)).thenReturn(false);

        NotFound exception = assertThrows(NotFound.class, () -> pizzaService.deletePizzaById(id));
        assertEquals("La pizza n'existe pas", exception.getMessage());
        verify(pizzaRepository).existsById(id);
        verify(pizzaRepository, never()).deleteById(any());
    }

    // Tests pour updatePizza
    @Test
    public void testUpdatePizzaSuccess() {
        Long id = 1L;

        Pizza oldPizza = new Pizza();
        oldPizza.setId(id);
        oldPizza.setNom("Margherita");
        oldPizza.setDescription("Pizza classique");
        oldPizza.setPhoto("old-photo.jpg");

        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("Margherita Spéciale");
        requestDto.setDescription("Pizza classique améliorée");
        Set<Long> standardIngredients = new HashSet<>(Arrays.asList(1L, 2L));
        requestDto.setStandardIngredientsIds(standardIngredients);
        Set<Long> optionalIngredients = new HashSet<>(Arrays.asList(3L, 4L));
        requestDto.setOptionalIngredientsIds(optionalIngredients);
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        String newFileName = "new-photo.jpg";
        when(imageService.saveImage(photoFile)).thenReturn(newFileName);

        Pizza newPizza = new Pizza();
        newPizza.setNom("Margherita Spéciale");
        newPizza.setDescription("Pizza classique améliorée");

        Pizza savedPizza = new Pizza();
        savedPizza.setId(id);
        savedPizza.setNom("Margherita Spéciale");
        savedPizza.setDescription("Pizza classique améliorée");
        savedPizza.setPhoto(newFileName);

        PizzaDto dto = new PizzaDto();
        dto.setId(id);
        dto.setNom("Margherita Spéciale");
        dto.setDescription("Pizza classique améliorée");
        dto.setPhoto(newFileName);

        when(pizzaRepository.findById(id)).thenReturn(Optional.of(oldPizza));
        when(pizzaMapper.toEntity(requestDto)).thenReturn(newPizza);
        when(pizzaRepository.save(any(Pizza.class))).thenReturn(savedPizza);
        when(pizzaMapper.toDto(savedPizza)).thenReturn(dto);

        PizzaDto result = pizzaService.updatePizza(id, requestDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Margherita Spéciale", result.getNom());
        assertEquals("Pizza classique améliorée", result.getDescription());
        assertEquals(newFileName, result.getPhoto());

        verify(pizzaRepository).findById(id);
        verify(imageService).saveImage(photoFile);
        verify(pizzaMapper).toEntity(requestDto);
        verify(pizzaRepository).save(any(Pizza.class));
        verify(pizzaMapper).toDto(savedPizza);
    }

    @Test
    public void testUpdatePizzaKeepOldPhoto() {
        Long id = 1L;

        Pizza oldPizza = new Pizza();
        oldPizza.setId(id);
        oldPizza.setNom("Margherita");
        oldPizza.setDescription("Pizza classique");
        oldPizza.setPhoto("old-photo.jpg");

        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("Margherita Spéciale");
        requestDto.setDescription("Pizza classique améliorée");
        Set<Long> standardIngredients = new HashSet<>(Arrays.asList(1L, 2L));
        requestDto.setStandardIngredientsIds(standardIngredients);
        Set<Long> optionalIngredients = new HashSet<>(Arrays.asList(3L, 4L));
        requestDto.setOptionalIngredientsIds(optionalIngredients);
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        when(imageService.saveImage(photoFile)).thenReturn(null);

        Pizza newPizza = new Pizza();
        newPizza.setNom("Margherita Spéciale");
        newPizza.setDescription("Pizza classique améliorée");

        Pizza savedPizza = new Pizza();
        savedPizza.setId(id);
        savedPizza.setNom("Margherita Spéciale");
        savedPizza.setDescription("Pizza classique améliorée");
        savedPizza.setPhoto("old-photo.jpg");

        PizzaDto dto = new PizzaDto();
        dto.setId(id);
        dto.setNom("Margherita Spéciale");
        dto.setDescription("Pizza classique améliorée");
        dto.setPhoto("old-photo.jpg");

        when(pizzaRepository.findById(id)).thenReturn(Optional.of(oldPizza));
        when(pizzaMapper.toEntity(requestDto)).thenReturn(newPizza);
        when(pizzaRepository.save(any(Pizza.class))).thenReturn(savedPizza);
        when(pizzaMapper.toDto(savedPizza)).thenReturn(dto);

        PizzaDto result = pizzaService.updatePizza(id, requestDto);

        assertNotNull(result);
        assertEquals("old-photo.jpg", result.getPhoto());

        verify(pizzaRepository).findById(id);
        verify(imageService).saveImage(photoFile);
        verify(pizzaMapper).toEntity(requestDto);
        verify(pizzaRepository).save(any(Pizza.class));
        verify(pizzaMapper).toDto(savedPizza);
    }

    @Test
    public void testUpdatePizzaNotFound() {
        Long id = 1L;
        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("Margherita");
        requestDto.setDescription("Pizza classique");

        when(pizzaRepository.findById(id)).thenReturn(Optional.empty());

        PizzaDto result = pizzaService.updatePizza(id, requestDto);

        assertNull(result);
        verify(pizzaRepository).findById(id);
        verify(imageService, never()).saveImage(any());
        verify(pizzaMapper, never()).toEntity(any());
        verify(pizzaRepository, never()).save(any());
    }

    @Test
    public void testUpdatePizzaNomNull() {
        Long id = 1L;

        Pizza oldPizza = new Pizza();
        oldPizza.setId(id);
        oldPizza.setNom("Margherita");
        oldPizza.setDescription("Pizza classique");
        oldPizza.setPhoto("old-photo.jpg");

        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom(null);
        requestDto.setDescription("Pizza classique améliorée");
        Set<Long> standardIngredients = new HashSet<>(Arrays.asList(1L, 2L));
        requestDto.setStandardIngredientsIds(standardIngredients);
        Set<Long> optionalIngredients = new HashSet<>(Arrays.asList(3L, 4L));
        requestDto.setOptionalIngredientsIds(optionalIngredients);
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        String newFileName = "new-photo.jpg";
        when(imageService.saveImage(photoFile)).thenReturn(newFileName);
        when(pizzaRepository.findById(id)).thenReturn(Optional.of(oldPizza));

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class,
                () -> pizzaService.updatePizza(id, requestDto));
        assertTrue(exception.getMessage().contains("Le nom de la pizza est obligatoire"));

        verify(pizzaRepository).findById(id);
        verify(imageService).saveImage(photoFile);
        verify(pizzaRepository, never()).save(any());
    }

    @Test
    public void testUpdatePizzaDescriptionNull() {
        Long id = 1L;

        Pizza oldPizza = new Pizza();
        oldPizza.setId(id);
        oldPizza.setNom("Margherita");
        oldPizza.setDescription("Pizza classique");
        oldPizza.setPhoto("old-photo.jpg");

        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("Margherita Spéciale");
        requestDto.setDescription(null);
        Set<Long> standardIngredients = new HashSet<>(Arrays.asList(1L, 2L));
        requestDto.setStandardIngredientsIds(standardIngredients);
        Set<Long> optionalIngredients = new HashSet<>(Arrays.asList(3L, 4L));
        requestDto.setOptionalIngredientsIds(optionalIngredients);
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        String newFileName = "new-photo.jpg";
        when(imageService.saveImage(photoFile)).thenReturn(newFileName);
        when(pizzaRepository.findById(id)).thenReturn(Optional.of(oldPizza));

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class,
                () -> pizzaService.updatePizza(id, requestDto));
        assertTrue(exception.getMessage().contains("La description de la pizza est obligatoire"));

        verify(pizzaRepository).findById(id);
        verify(imageService).saveImage(photoFile);
        verify(pizzaRepository, never()).save(any());
    }

    @Test
    public void testUpdatePizzaStandardIngredientsNull() {
        Long id = 1L;

        Pizza oldPizza = new Pizza();
        oldPizza.setId(id);
        oldPizza.setNom("Margherita");
        oldPizza.setDescription("Pizza classique");
        oldPizza.setPhoto("old-photo.jpg");

        PizzaRequestDto requestDto = new PizzaRequestDto();
        requestDto.setNom("Margherita Spéciale");
        requestDto.setDescription("Pizza classique");
        MultipartFile photoFile = mock(MultipartFile.class);
        requestDto.setPhoto(photoFile);

        String newFileName = "new-photo.jpg";
        when(imageService.saveImage(photoFile)).thenReturn(newFileName);
        when(pizzaRepository.findById(id)).thenReturn(Optional.of(oldPizza));

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class,
                () -> pizzaService.updatePizza(id, requestDto));
        assertTrue(exception.getMessage().contains("Les ingrédients de base de la pizza sont obligatoires"));

        verify(pizzaRepository).findById(id);
        verify(imageService).saveImage(photoFile);
        verify(pizzaRepository, never()).save(any());
    }
}
