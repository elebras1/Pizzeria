package com.projetm1.pizzeria.configuration;

import com.projetm1.pizzeria.commande.Commande;
import com.projetm1.pizzeria.commande.CommandeRepository;
import com.projetm1.pizzeria.commentaire.Commentaire;
import com.projetm1.pizzeria.commentaire.CommentaireRepository;
import com.projetm1.pizzeria.compte.Compte;
import com.projetm1.pizzeria.compte.CompteRepository;
import com.projetm1.pizzeria.ingredient.Ingredient;
import com.projetm1.pizzeria.ingredient.IngredientRepository;
import com.projetm1.pizzeria.pizza.Pizza;
import com.projetm1.pizzeria.pizza.PizzaRepository;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanier;
import com.projetm1.pizzeria.pizzaPanier.PizzaPanierRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {
    private final PizzaRepository pizzaRepository;
    private final IngredientRepository ingredientRepository;
    private final PizzaPanierRepository pizzaPanierRepository;
    private final CommandeRepository commandeRepository;
    private final CompteRepository compteRepository;
    private final CommentaireRepository commentaireRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PizzaRepository pizzaRepository, IngredientRepository ingredientRepository, PizzaPanierRepository pizzaPanierRepository, CommandeRepository commandeRepository, CompteRepository compteRepository, CommentaireRepository commentaireRepository, PasswordEncoder passwordEncoder) {
        this.pizzaRepository = pizzaRepository;
        this.ingredientRepository = ingredientRepository;
        this.pizzaPanierRepository = pizzaPanierRepository;
        this.commandeRepository = commandeRepository;
        this.compteRepository = compteRepository;
        this.commentaireRepository = commentaireRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(this.createIngredient("Tomate", "Tomate fraîche", 0.5f));
        ingredients.add(this.createIngredient("Mozzarella", "Mozzarella de bufflonne", 1.0f));
        ingredients.add(this.createIngredient("Basilic", "Feuilles de basilic", 0.2f));
        ingredients.add(this.createIngredient("Pepperoni", "Tranches de pepperoni", 1.5f));
        ingredients.add(this.createIngredient("Jambon", "Jambon de pays", 1.2f));
        ingredients.add(this.createIngredient("Champignon", "Champignons frais", 0.8f));
        ingredients.add(this.createIngredient("Oignon", "Oignons rouges", 0.3f));
        ingredients.add(this.createIngredient("Poivron", "Poivrons verts", 0.4f));
        ingredients.add(this.createIngredient("Olives", "Olives noires", 0.6f));
        ingredients.add(this.createIngredient("Anchois", "Anchois salés", 1.0f));
        this.ingredientRepository.saveAll(ingredients);

        List<Pizza> pizzas = new ArrayList<>();
        pizzas.add(this.createPizza("Margherita", "Classique italienne", "margherita.jpg",
                Set.of(ingredients.get(0), ingredients.get(1), ingredients.get(2)),
                Set.of(ingredients.get(8), ingredients.get(6))));

        pizzas.add(this.createPizza("Pepperoni", "Pizza au pepperoni", "pepperoni.jpg",
                Set.of(ingredients.get(0), ingredients.get(1), ingredients.get(3)),
                Set.of(ingredients.get(7))));

        pizzas.add(this.createPizza("Reine", "Pizza Reine avec jambon et champignons", "reine.jpg",
                Set.of(ingredients.get(0), ingredients.get(1), ingredients.get(4), ingredients.get(5)),
                Set.of(ingredients.get(9))));

        this.pizzaRepository.saveAll(pizzas);

        Compte client1 = this.createCompte("jdoe", "John", "Doe", "password123", false);
        Compte client2 = this.createCompte("asmith", "Alice", "Smith", "securepass", false);
        Compte admin = this.createCompte("admin", "Super", "Admin", "adminpass", true);
        this.compteRepository.saveAll(List.of(client1, client2, admin));

        Commande commande1 = this.createCommande(client1);
        Commande commande2 = this.createCommande(client2);
        this.commandeRepository.saveAll(List.of(commande1, commande2));

        PizzaPanier panier1 = this.createPizzaPanier(commande1, pizzas.get(0), Set.of(ingredients.get(8)));
        PizzaPanier panier2 = this.createPizzaPanier(commande1, pizzas.get(2), Set.of());
        PizzaPanier panier3 = this.createPizzaPanier(commande2, pizzas.get(1), Set.of(ingredients.get(7)));
        this.pizzaPanierRepository.saveAll(List.of(panier1, panier2, panier3));

        Commentaire com1 = this.createCommentaire(String.valueOf(commande1.getId()), "Délicieuse pizza, super croustillante !");
        Commentaire com2 = this.createCommentaire(String.valueOf(commande2.getId()), "Un peu trop salée à mon goût.");
        this.commentaireRepository.saveAll(List.of(com1, com2));

        System.out.println("Jeu de données initialisé !");
    }

    private Ingredient createIngredient(String nom, String description, float prix) {
        Ingredient ingredient = new Ingredient();
        ingredient.setNom(nom);
        ingredient.setDescription(description);
        ingredient.setPrix(prix);
        return ingredient;
    }

    private Pizza createPizza(String nom, String description, String photo, Set<Ingredient> standard, Set<Ingredient> optional) {
        Pizza pizza = new Pizza();
        pizza.setNom(nom);
        pizza.setDescription(description);
        pizza.setPhoto(photo);
        pizza.setStandardIngredients(standard);
        pizza.setOptionalIngredients(optional);
        return pizza;
    }

    private Compte createCompte(String pseudo, String prenom, String nom, String motDePasse, boolean isAdmin) {
        Compte compte = new Compte();
        compte.setPseudo(pseudo);
        compte.setPrenom(prenom);
        compte.setNom(nom);
        compte.setMotDePasse(this.passwordEncoder.encode(motDePasse));
        compte.setIsAdmin(isAdmin);
        return compte;
    }

    private Commande createCommande(Compte compte) {
        Commande commande = new Commande();
        commande.setCompte(compte);
        return commande;
    }

    private PizzaPanier createPizzaPanier(Commande commande, Pizza pizza, Set<Ingredient> ingredients) {
        Set<Ingredient> finalIngredients = new HashSet<>(ingredients);
        finalIngredients.addAll(pizza.getStandardIngredients());
        PizzaPanier pizzaPanier = new PizzaPanier();
        pizzaPanier.setCommande(commande);
        pizzaPanier.setPizza(pizza);
        pizzaPanier.setIngredients(finalIngredients);
        return pizzaPanier;
    }


    private Commentaire createCommentaire(String idCommande, String texte) {
        Commentaire commentaire = new Commentaire();
        commentaire.setIdCommande(idCommande);
        commentaire.setTexte(texte);
        return commentaire;
    }

}
