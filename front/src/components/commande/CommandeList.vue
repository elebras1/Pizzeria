<template>
    <div v-if="isAdmin">
        <h1>Liste des Commandes Payées</h1>
        <div v-for="commande in commandesPayees" :key="commande.id" class="commande">
            <h2>Commande #{{ commande.id }}</h2>
            <p>
                <strong>Compte : </strong>
                <span v-if="commande.compte">
                    {{ commande.compte.prenom }} {{ commande.compte.nom }} ({{ commande.compte.pseudo }})
                </span>
                <span v-else>
                    Compte inconnu
                </span>
            </p>
            <p>
                <strong>En cours : </strong>
                <span>{{ commande.enCours ? 'Oui' : 'Non' }}</span>
            </p>
            <p>
                <strong>Payée : </strong>
                <span>{{ commande.isPaye ? 'Oui' : 'Non' }}</span>
            </p>
            <p>
                <strong>Date de commande : </strong>
                <span>{{ formatDate(commande.date) }}</span>
            </p>
            <p>
                <strong>Pizzas commandées : </strong>
                <span v-for="(groupe, index) in commande.pizzasGroupees" :key="index">
                    {{ groupe.nom }} x{{ groupe.quantite }}<span v-if="index < commande.pizzasGroupees.length - 1">, </span>
                </span>
            </p>
            <button @click="togglePizzaDetails(commande)">Détails pizzas</button>
            <div v-if="commande.showPizzaDetails" class="details-pizzas">
                <div v-for="(groupe, index) in commande.pizzasGroupees" :key="index" class="pizza-detail">
                    <h3>{{ groupe.nom }} x{{ groupe.quantite }}</h3>
                    <p>
                        <strong>Ingrédients : </strong>
                        <span v-for="(ingredient, i) in groupe.ingredients" :key="i">
                            {{ ingredient.nom }}<span v-if="i < groupe.ingredients.length - 1">, </span>
                        </span>
                    </p>
                </div>
            </div>
            <button @click="toggleComments(commande)">Afficher commentaire</button>
            <div v-if="commande.showComments" class="details-comments">
                <div v-if="commande.comments && commande.comments.length > 0">
                    <div v-for="(comment, i) in commande.comments" :key="i" class="comment">
                        <p>{{ comment.texte }}</p>
                        <img v-if="comment.photoUrl" :src="comment.photoUrl" alt="photo du commentaire" />
                    </div>
                </div>
                <div v-else>
                    <p>Aucun commentaire</p>
                </div>
            </div>
            <button v-if="commande.enCours" @click="finishCommande(commande)">Terminer la commande</button>
            <hr>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import commandeService from '@/services/commandeService';
import compteService from '@/services/compteService';
import imageService from '@/services/imageService';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();
const isAdmin = ref(false);
const commandes = ref([]);

const ingredientsEqual = (ingredients1, ingredients2) => {
    if (!ingredients1 || !ingredients2) return false;
    if (ingredients1.length !== ingredients2.length) return false;
    
    const ids1 = new Set(ingredients1.map(ing => ing.id));
    const ids2 = new Set(ingredients2.map(ing => ing.id));
    
    for (const id of ids1) {
        if (!ids2.has(id)) return false;
    }
    
    return true;
};

const grouperPizzas = (panier) => {
    const groupes = [];
    
    for (const item of panier) {
        const pizza = item.pizza;
        const ingredients = item.ingredients;
        
        const groupeExistant = groupes.find(groupe => 
            groupe.nom === pizza.nom && ingredientsEqual(groupe.ingredients, ingredients)
        );
        
        if (groupeExistant) {
            groupeExistant.quantite += 1;
        } else {
            groupes.push({
                nom: pizza.nom,
                ingredients: ingredients,
                quantite: 1
            });
        }
    }
    
    return groupes;
};

const commandesPayees = computed(() => {
    return commandes.value
        .filter(commande => commande.isPaye)
        .sort((a, b) => {
            if (a.enCours === b.enCours) return 0;
            return a.enCours ? -1 : 1;
        });
});

const fetchCommandes = () => {
    const idCompte = route.params.idCompte;
    let promise;

    if (idCompte) {
        promise = compteService.getCommandesById(idCompte);
    } else {
        promise = commandeService.getCommandes();
    }

    promise
        .then(response => {
            commandes.value = response.data.map(commande => {
                const pizzasGroupees = grouperPizzas(commande.panier);
                
                return {
                    ...commande,
                    showPizzaDetails: false,
                    showComments: false,
                    compte: null,
                    comments: null,
                    pizzasGroupees
                };
            });

            if (commandes.value.length > 0) {
                const compteIds = [...new Set(commandes.value.map(c => c.compteId))];
                Promise.all(compteIds.map(id => compteService.getCompte(id)))
                    .then(results => {
                        const compteMap = Object.fromEntries(results.map(res => [res.data.id, res.data]));
                        commandes.value.forEach(commande => {
                            commande.compte = compteMap[commande.compteId] || null;
                        });
                    })
                    .catch(error => {
                        console.error("Erreur lors de la récupération des comptes", error);
                    });

                commandes.value.forEach(commande => {
                    commandeService.getCommentaires(commande.id)
                        .then(response => {
                            commande.comments = response.data;
                            commande.comments.forEach(comment => {
                                if (comment.photo) {
                                    imageService.getImage(comment.photo)
                                        .then(response => {
                                            const url = URL.createObjectURL(response.data);
                                            comment.photoUrl = url;
                                        })
                                        .catch(error => {
                                            console.error(`Erreur lors du chargement de l'image pour le commentaire ${comment.id}`, error);
                                        });
                                }
                            });
                        })
                        .catch(error => {
                            console.error(`Erreur lors de la récupération des commentaires pour la commande ${commande.id}`, error);
                        });
                });
            }
        })
        .catch(error => {
            console.error("Erreur lors de la récupération des commandes", error);
        });
};

const togglePizzaDetails = (commande) => {
    commande.showPizzaDetails = !commande.showPizzaDetails;
};

const toggleComments = (commande) => {
    commande.showComments = !commande.showComments;
};

const formatDate = (date) => {
    if (!date) return '';
    const parsedDate = new Date(date);
    return parsedDate.toLocaleString('fr-FR', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        second: 'numeric'
    });
};

const finishCommande = (commande) => {
    commandeService.finishCommande(commande.id)
        .then(response => {
            commande.enCours = false;
        })
        .catch(error => {
            console.error("Erreur lors de la fin de la commande", error);
        });
};

onMounted(async () => {
    try {
        const response = await authStore.verifyAdmin();
        const compteDto = response.data;
        if (compteDto && compteDto.isAdmin) {
            isAdmin.value = true;
            fetchCommandes();
        } else {
            router.push({ name: 'Home' });
        }
    } catch (error) {
        console.error("Erreur lors de la vérification admin :", error);
        router.push({ name: 'Home' });
    }
});
</script>

<style scoped>
.commande {
    margin-bottom: 1.5em;
    padding: 1em;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.details-pizzas,
.details-comments {
    margin-top: 0.5em;
    padding-left: 1em;
    background: #f9f9f9;
    border-left: 3px solid #ccc;
}

.pizza-detail,
.comment {
    margin-bottom: 0.5em;
}

button {
  background-color: #555;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
  margin: 1px;
}

button:hover {
  background-color: #777;
}

.comment img {
    max-width: 100%;
    max-height: 200px;
    object-fit: contain;
}
</style>