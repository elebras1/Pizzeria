package com.projetm1.pizzeria.commentaire;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.projetm1.pizzeria.commentaire.Commentaire;
import com.projetm1.pizzeria.commentaire.CommentaireMapper;
import com.projetm1.pizzeria.commentaire.CommentaireRepository;
import com.projetm1.pizzeria.commentaire.CommentaireService;
import com.projetm1.pizzeria.commentaire.dto.CommentaireDto;
import com.projetm1.pizzeria.commentaire.dto.CommentaireRequestDto;
import com.projetm1.pizzeria.commande.Commande;
import com.projetm1.pizzeria.commande.CommandeRepository;
import com.projetm1.pizzeria.error.NotFound;
import com.projetm1.pizzeria.error.UnprocessableEntity;
import com.projetm1.pizzeria.image.ImageService;
import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

public class CommentaireServiceTest {

    @InjectMocks
    private CommentaireService commentaireService;

    @Mock
    private CommentaireRepository commentaireRepository;

    @Mock
    private CommentaireMapper commentaireMapper;

    @Mock
    private CommandeRepository commandeRepository;

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

    // ========================= getCommentaireById =========================

    @Test
    public void testGetCommentaireByIdFound() {
        String id = "abc123";
        Commentaire commentaire = new Commentaire();
        commentaire.setId(id);
        CommentaireDto dto = new CommentaireDto();
        dto.setId(id);

        when(commentaireRepository.findById(id)).thenReturn(Optional.of(commentaire));
        when(commentaireMapper.toDto(commentaire)).thenReturn(dto);

        CommentaireDto result = commentaireService.getCommentaireById(id);
        assertEquals(id, result.getId());
    }

    @Test
    public void testGetCommentaireByIdNotFound() {
        String id = "abc123";
        when(commentaireRepository.findById(id)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () ->
                commentaireService.getCommentaireById(id)
        );
        assertEquals("Le commentaire n'existe pas", exception.getMessage());
    }

    // ========================= getAllCommentaires =========================

    @Test
    public void testGetAllCommentairesEmpty() {
        when(commentaireRepository.findAll()).thenReturn(Collections.emptyList());
        List<CommentaireDto> dtos = commentaireService.getAllCommentaires();
        assertTrue(dtos.isEmpty());
    }

    @Test
    public void testGetAllCommentairesNonEmpty() {
        Commentaire c1 = new Commentaire();
        c1.setId("1");
        Commentaire c2 = new Commentaire();
        c2.setId("2");
        CommentaireDto dto1 = new CommentaireDto();
        dto1.setId("1");
        CommentaireDto dto2 = new CommentaireDto();
        dto2.setId("2");
        when(commentaireRepository.findAll()).thenReturn(Arrays.asList(c1, c2));
        when(commentaireMapper.toDto(c1)).thenReturn(dto1);
        when(commentaireMapper.toDto(c2)).thenReturn(dto2);

        List<CommentaireDto> dtos = commentaireService.getAllCommentaires();
        assertEquals(2, dtos.size());
        assertEquals("1", dtos.get(0).getId());
        assertEquals("2", dtos.get(1).getId());
    }

    // ========================= saveCommentaire =========================

    @Test
    public void testSaveCommentaireCommandeNotFound() {
        Long commandeId = 1L;
        CommentaireRequestDto requestDto = new CommentaireRequestDto();
        requestDto.setTexte("Un commentaire");

        when(commandeRepository.findById(commandeId)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () ->
                commentaireService.saveCommentaire(commandeId, requestDto)
        );
        assertEquals("La commande n'existe pas", exception.getMessage());
    }

    @Test
    public void testSaveCommentaireEmptyTexte() {
        Long commandeId = 1L;
        CommentaireRequestDto requestDto = new CommentaireRequestDto();
        requestDto.setTexte(""); // Texte vide

        // Simuler l'existence de la commande
        Commande commande = new Commande();
        when(commandeRepository.findById(commandeId)).thenReturn(Optional.of(commande));

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () ->
                commentaireService.saveCommentaire(commandeId, requestDto)
        );
        assertEquals("Le texte du commentaire est obligatoire", exception.getMessage());
    }

    @Test
    public void testSaveCommentaireSuccess() {
        Long commandeId = 1L;
        CommentaireRequestDto requestDto = new CommentaireRequestDto();
        requestDto.setTexte("Un commentaire valide");
        // On simule un fichier photo (peut être null dans ce test)
        MultipartFile fakePhoto = mock(MultipartFile.class);
        requestDto.setPhoto(fakePhoto);

        // Simuler la commande existante
        Commande commande = new Commande();
        // Simuler la liste des ids de commentaires inexistante au départ
        commande.setIdCommentaires(null);
        when(commandeRepository.findById(commandeId)).thenReturn(Optional.of(commande));

        // Simuler la transformation du DTO en entité
        Commentaire commentaireEntity = new Commentaire();
        when(commentaireMapper.toEntity(requestDto)).thenReturn(commentaireEntity);

        // Simuler le saving de l'image
        String fileName = "photo.jpg";
        when(imageService.saveImage(fakePhoto)).thenReturn(fileName);

        // Lors du saving du commentaire, on attribue un id
        commentaireEntity.setId("com123");
        // Simuler le save de commentaire
        when(commentaireRepository.save(commentaireEntity)).thenReturn(commentaireEntity);

        // Simuler le mapping vers DTO
        CommentaireDto dto = new CommentaireDto();
        dto.setId("com123");
        when(commentaireMapper.toDto(commentaireEntity)).thenReturn(dto);

        // Simuler la mise à jour de la commande avec ajout de l'id du commentaire
        // Au préalable, commande.getIdCommentaires() sera null, on s'assure qu'une nouvelle liste est créée
        when(commandeRepository.save(commande)).thenReturn(commande);

        CommentaireDto result = commentaireService.saveCommentaire(commandeId, requestDto);
        assertNotNull(result);
        assertEquals("com123", result.getId());
        // Vérification que l'image a été sauvegardée et attribuée à l'entité
        verify(imageService, times(1)).saveImage(fakePhoto);
        // Vérifier que l'id du commentaire a été ajouté à la commande
        assertNotNull(commande.getIdCommentaires());
        assertTrue(commande.getIdCommentaires().contains("com123"));
    }

    // ========================= deleteCommentaireById =========================

    @Test
    public void testDeleteCommentaireByIdNotFound() {
        String id = "com123";
        when(commentaireRepository.findById(id)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () ->
                commentaireService.deleteCommentaireById(id)
        );
        assertEquals("Le commentaire n'existe pas", exception.getMessage());
    }

    @Test
    public void testDeleteCommentaireByIdSuccessWithCommandeUpdate() {
        String id = "com123";
        Commentaire commentaire = new Commentaire();
        commentaire.setId(id);
        // Simuler qu'il existe un idCommande valide
        commentaire.setIdCommande("1");

        // Simuler la présence du commentaire dans la base
        when(commentaireRepository.findById(id)).thenReturn(Optional.of(commentaire));

        // Simuler la récupération de la commande correspondante
        Commande commande = new Commande();
        List<String> idCommentaires = new ArrayList<>();
        idCommentaires.add(id);
        commande.setIdCommentaires(idCommentaires);
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        // Appel de la méthode
        commentaireService.deleteCommentaireById(id);

        // Vérifier que l'id a bien été supprimé de la liste de la commande et que la commande a été sauvegardée
        assertFalse(commande.getIdCommentaires().contains(id));
        verify(commandeRepository, times(1)).save(commande);
        // Vérifier que le commentaire a été supprimé
        verify(commentaireRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteCommentaireByIdNumberFormatException() {
        String id = "com123";
        Commentaire commentaire = new Commentaire();
        commentaire.setId(id);
        // Simuler un idCommande invalide (non convertible en Long)
        commentaire.setIdCommande("invalid");
        when(commentaireRepository.findById(id)).thenReturn(Optional.of(commentaire));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                commentaireService.deleteCommentaireById(id)
        );
        assertEquals("Erreur lors de la conversion de l'identifiant de la commande", exception.getMessage());
        // On ne vérifie pas la suppression car l'exception est levée avant
    }

    // ========================= updateCommentaire =========================

    @Test
    public void testUpdateCommentaireNotFound() {
        String id = "com123";
        CommentaireRequestDto requestDto = new CommentaireRequestDto();
        requestDto.setTexte("Nouveau texte");
        when(commentaireRepository.findById(id)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () ->
                commentaireService.updateCommentaire(id, requestDto)
        );
        assertEquals("Le commentaire n'existe pas", exception.getMessage());
    }

    @Test
    public void testUpdateCommentaireEmptyTexte() {
        String id = "com123";
        CommentaireRequestDto requestDto = new CommentaireRequestDto();
        requestDto.setTexte(""); // Texte vide

        Commentaire existing = new Commentaire();
        existing.setId(id);
        existing.setIdCommande("1");
        existing.setPhoto("oldPhoto.jpg");
        when(commentaireRepository.findById(id)).thenReturn(Optional.of(existing));

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class, () ->
                commentaireService.updateCommentaire(id, requestDto)
        );
        assertEquals("Le texte du commentaire est obligatoire", exception.getMessage());
    }

    @Test
    public void testUpdateCommentaireSuccessWithNewPhoto() {
        String id = "com123";
        CommentaireRequestDto requestDto = new CommentaireRequestDto();
        requestDto.setTexte("Texte mis à jour");
        // Simuler un nouveau fichier photo
        MultipartFile newPhoto = mock(MultipartFile.class);
        requestDto.setPhoto(newPhoto);

        // Commentaire existant
        Commentaire existing = new Commentaire();
        existing.setId(id);
        existing.setIdCommande("1");
        existing.setPhoto("oldPhoto.jpg");
        when(commentaireRepository.findById(id)).thenReturn(Optional.of(existing));

        // Simulation de la conversion du DTO en entité
        Commentaire updatedEntity = new Commentaire();
        when(commentaireMapper.toEntity(requestDto)).thenReturn(updatedEntity);

        // Simuler que l'image est sauvegardée avec succès et renvoie un nouveau nom
        String newFileName = "newPhoto.jpg";
        when(imageService.saveImage(newPhoto)).thenReturn(newFileName);

        // Lors du save, on simule que l'entité est retournée avec l'id
        updatedEntity.setId(id);
        updatedEntity.setIdCommande("1");
        updatedEntity.setPhoto(newFileName);
        when(commentaireRepository.save(updatedEntity)).thenReturn(updatedEntity);

        // Simuler le mapping vers DTO
        CommentaireDto updatedDto = new CommentaireDto();
        updatedDto.setId(id);
        updatedDto.setPhoto(newFileName);
        updatedDto.setTexte(requestDto.getTexte());
        when(commentaireMapper.toDto(updatedEntity)).thenReturn(updatedDto);

        CommentaireDto result = commentaireService.updateCommentaire(id, requestDto);
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(newFileName, result.getPhoto());
        assertEquals(requestDto.getTexte(), result.getTexte());
    }

    @Test
    public void testUpdateCommentaireSuccessWithoutNewPhoto() {
        String id = "com123";
        CommentaireRequestDto requestDto = new CommentaireRequestDto();
        requestDto.setTexte("Texte mis à jour");
        // Aucun nouveau fichier photo
        requestDto.setPhoto(null);

        // Commentaire existant
        Commentaire existing = new Commentaire();
        existing.setId(id);
        existing.setIdCommande("1");
        existing.setPhoto("oldPhoto.jpg");
        when(commentaireRepository.findById(id)).thenReturn(Optional.of(existing));

        // Conversion du DTO en entité
        Commentaire updatedEntity = new Commentaire();
        when(commentaireMapper.toEntity(requestDto)).thenReturn(updatedEntity);

        // Simuler que l'imageService renvoie null (aucune mise à jour de photo)
        when(imageService.saveImage(null)).thenReturn(null);

        updatedEntity.setId(id);
        updatedEntity.setIdCommande("1");
        // On garde l'ancienne photo
        updatedEntity.setPhoto(existing.getPhoto());
        when(commentaireRepository.save(updatedEntity)).thenReturn(updatedEntity);

        CommentaireDto updatedDto = new CommentaireDto();
        updatedDto.setId(id);
        updatedDto.setPhoto(existing.getPhoto());
        updatedDto.setTexte(requestDto.getTexte());
        when(commentaireMapper.toDto(updatedEntity)).thenReturn(updatedDto);

        CommentaireDto result = commentaireService.updateCommentaire(id, requestDto);
        assertNotNull(result);
        assertEquals("oldPhoto.jpg", result.getPhoto());
        assertEquals(requestDto.getTexte(), result.getTexte());
    }

    // ========================= getCommentairesByCommandeId =========================

    @Test
    public void testGetCommentairesByCommandeIdCommandeNotFound() {
        Long commandeId = 1L;
        when(commandeRepository.findById(commandeId)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () ->
                commentaireService.getCommentairesByCommandeId(commandeId)
        );
        assertEquals("La commande n'existe pas", exception.getMessage());
    }

    @Test
    public void testGetCommentairesByCommandeIdNoCommentaires() {
        Long commandeId = 1L;
        Commande commande = new Commande();
        // Liste d'identifiants vide
        commande.setIdCommentaires(new ArrayList<>());
        when(commandeRepository.findById(commandeId)).thenReturn(Optional.of(commande));

        List<CommentaireDto> result = commentaireService.getCommentairesByCommandeId(commandeId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCommentairesByCommandeIdSuccess() {
        Long commandeId = 1L;
        Commande commande = new Commande();
        List<String> commentairesIds = Arrays.asList("com1", "com2");
        commande.setIdCommentaires(commentairesIds);
        when(commandeRepository.findById(commandeId)).thenReturn(Optional.of(commande));

        Commentaire c1 = new Commentaire();
        c1.setId("com1");
        Commentaire c2 = new Commentaire();
        c2.setId("com2");
        when(commentaireRepository.findById("com1")).thenReturn(Optional.of(c1));
        when(commentaireRepository.findById("com2")).thenReturn(Optional.of(c2));

        CommentaireDto dto1 = new CommentaireDto();
        dto1.setId("com1");
        CommentaireDto dto2 = new CommentaireDto();
        dto2.setId("com2");
        when(commentaireMapper.toDto(c1)).thenReturn(dto1);
        when(commentaireMapper.toDto(c2)).thenReturn(dto2);

        List<CommentaireDto> result = commentaireService.getCommentairesByCommandeId(commandeId);
        assertEquals(2, result.size());
        assertEquals("com1", result.get(0).getId());
        assertEquals("com2", result.get(1).getId());
    }
}
