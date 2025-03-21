# Pizzeria
[![java](https://img.shields.io/badge/Java-orange.svg)](https://www.java.com)
[![Vuejs](https://img.shields.io/badge/Vue.js-green.svg)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-lightgreen.svg)](https://spring.io/projects/spring-boot)
[![MariaDB](https://img.shields.io/badge/MariaDB-blue.svg)](https://mariadb.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-darkgreen.svg)](https://www.mongodb.com/)

## Description Générale
Ce projet est une application web permettant la commande de pizzas personnalisées. Deux types d'utilisateurs interagissent avec le système : les clients, qui s'authentifient, qui consultent le menu, passent commande et laissent des commentaires, et l'administrateur, qui gère les pizzas, les ingrédients, les commandes, les comptes et les statistiques de ventes.

## Technologies utilisées

Java 21 <br>
NodeJS <br>
ExpressJs<br>
MariaDB<br>
MongoDB<br>
Maven<br>
Spring Boot <br>
Spring Data JPA <br>
Spring Data Mongo <br>
Spring Security <br>
Stripe API pour la gestion des paiements <br>
Jackson pour la gestion des objets JSON <br>
MapStruct Mappers pour la transformation entre entités et DTO <br>
Lombock pour les assesseurs,ect ...<br>
VueJS <br>
Vue-Router pour les route VueJS<br>
Chart-JS pour les statistiques<br>
JWT pour les tokens<br>
Axios pour les requetes<br>
Pinia pour le store des informations<br>


## Fonctionnalités
1. Gestion des Comptes (CompteService)

    Création de comptes utilisateurs avec vérification de l'unicité du pseudo.<br>
    Récupération des informations d'un compte par identifiant ou par token.<br>
    Mise à jour des informations d'un compte.<br>
    Changement de mot de passe avec vérification de l'ancien mot de passe.<br>
    Suppression d'un compte.<br>
    Gestion des rôles d'administration.<br>

2. Authentification (AuthentificationService)

    Vérification des identifiants et des mots de passe des utilisateurs.<br>
    Gestion de la connexion avec vérification de la correspondance entre le mot de passe saisi et le mot de passe enregistré.<br>

3. Gestion des Commandes (CommandeService)

    Création de commandes avec vérification des commandes en cours pour un compte.<br>
    Récupération d'une commande par identifiant ou de la commande en cours pour un utilisateur.<br>
    Récupération de toutes les commandes.<br>
    Mise à jour d'une commande (modification du panier).<br>
    Finalisation d'une commande.<br>
    Paiement d'une commande via Stripe avec génération d'une session de paiement.<br>
    Suppression d'une commande.<br>

4. Gestion des Pizzas (PizzaService)

    Création de nouvelles pizzas avec gestion des ingrédients standard et optionnels.<br>
    Récupération d'une pizza par identifiant.<br>
    Récupération de toutes les pizzas disponibles.<br>
    Mise à jour des informations d'une pizza.<br>
    Suppression d'une pizza.<br>

5. Gestion des Paniers de Pizzas (PizzaPanierService)

    Création de paniers de pizzas avec association d'ingrédients.<br>
    Récupération d'un panier de pizza par identifiant.<br>
    Récupération de tous les paniers de pizzas.<br>
    Mise à jour d'un panier de pizza.<br>
    Suppression d'un panier de pizza.<br>

6. Gestion des Ingrédients (IngredientService)

    Création de nouveaux ingrédients avec vérification d'unicité.<br>
    Récupération d'un ingrédient par identifiant.<br>
    Récupération de tous les ingrédients disponibles.<br>
    Mise à jour des informations d'un ingrédient.<br>
    Suppression d'un ingrédient avec vérification d'absence de références dans les pizzas.<br>

7. Gestion des Commentaires (CommentaireService)

    Création de commentaires associés à une commande, avec enregistrement éventuel d'une image.<br>
    Récupération d'un commentaire par identifiant.<br>
    Récupération de tous les commentaires.<br>
    Mise à jour des commentaires.<br>
    Suppression d'un commentaire avec mise à jour de la liste des commentaires de la commande associée.<br>

8. Gestion des Images (ImageService)

    Sauvegarde d'images sur le serveur.<br>
    Récupération d'une image par son nom de fichier.<br>
    Vérification des extensions d'image prises en charge (PNG, JPG, JPEG).<br>
    
## Base de données
<img src="database_pizzeria.png" alt="Texte alternatif" width="640" height="293">

## Auteurs
