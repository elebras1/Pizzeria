<template>
    <div>
        <h1>Liste des Commandes</h1>
        <div v-for="commande in commandes" :key="commande.id" class="commande">
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
                <strong>Pizzas commandées : </strong>
                <span v-for="(panier, index) in commande.panier" :key="index">
                    {{ panier.pizza.nom }}<span v-if="index < commande.panier.length - 1">, </span>
                </span>
            </p>
            <button @click="togglePizzaDetails(commande)">Détails pizzas</button>
            <div v-if="commande.showPizzaDetails" class="details-pizzas">
                <div v-for="(panier, index) in commande.panier" :key="index" class="pizza-detail">
                    <h3>{{ panier.pizza.nom }}</h3>
                    <p>
                        <strong>Ingrédients : </strong>
                        <span v-for="(ingredient, i) in panier.ingredients" :key="i">
                            {{ ingredient.nom }}<span v-if="i < panier.ingredients.length - 1">, </span>
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
            <hr>
        </div>
    </div>
</template>

<script>
import commandeService from '@/services/commandeService';
import compteService from '@/services/compteService';
import imageService from '@/services/imageService';

export default {
    name: 'CommandeList',
    data() {
        return {
            commandes: []
        };
    },
    methods: {
        fetchCommandes() {
            const idCompte = this.$route.params.idCompte;

            let promise;
            if (idCompte) {
                promise = compteService.getCommandes(idCompte);
            } else {
                promise = commandeService.getCommandes();
            }

            promise
                .then(response => {
                    this.commandes = response.data.map(commande => ({
                        ...commande,
                        showPizzaDetails: false,
                        showComments: false,
                        compte: null,
                        comments: null
                    }));

                    if (this.commandes.length > 0) {
                        const compteIds = [...new Set(this.commandes.map(c => c.compteId))];
                        Promise.all(compteIds.map(id => compteService.getCompte(id)))
                            .then(results => {
                                const compteMap = Object.fromEntries(results.map(res => [res.data.id, res.data]));

                                this.commandes.forEach(commande => {
                                    commande.compte = compteMap[commande.compteId] || null;
                                });
                            })
                            .catch(error => {
                                console.error("Erreur lors de la récupération des comptes", error);
                            });

                        this.commandes.forEach(commande => {
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
        },

        togglePizzaDetails(commande) {
            commande.showPizzaDetails = !commande.showPizzaDetails;
        },

        toggleComments(commande) {
            commande.showComments = !commande.showComments;
        }
    },
    mounted() {
        this.fetchCommandes();
    }
};
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
    margin-right: 0.5em;
    margin-top: 0.5em;
}

.comment img {
    max-width: 100%;
    max-height: 200px;
    object-fit: contain;
}
</style>
