import { defineStore } from 'pinia';
import api from "@/interceptors/api.js";
import { useAuthStore } from "@/stores/auth.js";

export const usePanierStore = defineStore('panier', {
    state: () => ({
        Panier: [],
    }),
    actions: {
        setPanier(data) {
            this.Panier = data;
            this.saveToLocalStorage();
        },
        addPizza(pizzaItem) {
            const existingPizza = this.Panier.find(item =>
                item.pizza.nom === pizzaItem.pizza.nom &&
                JSON.stringify(item.ingredients) === JSON.stringify(pizzaItem.ingredients)
            );
            if (existingPizza) {
                existingPizza.quantity += pizzaItem.quantity;
            } else {
                this.Panier.push(pizzaItem);
            }
            this.saveToLocalStorage();
            console.log('Panier après ajout :', this.Panier);
        },
        removePizza(pizza) {
            this.Panier = this.Panier.filter(item => item !== pizza);
            this.saveToLocalStorage();
        },
        clearPanier() {
            this.Panier = [];
            this.saveToLocalStorage();
        },
        getPanier() {
            return this.Panier;
        },
        async savePanier() {
            try {
                const res = await api.get('/commandes/enCours');

                if (res.data) {

                    const response = await api.put('/commandes', {
                        compteId: null,
                        commentairesIds: null,
                        panier: this.reverseTransformData(this.Panier),
                    });
                    this.saveToLocalStorage();
                    return true;

                } else {
                    const response = await api.post('/commandes', {
                        compteId: null,
                        commentairesIds: null,
                        panier: this.reverseTransformData(this.Panier),
                    });
                    this.saveToLocalStorage();
                    return true;
                }
            } catch (error) {
                console.error("Erreur lors de la création ou mise à jour de la commande :", error);
                return false;
            }
        },
        loadPanier() {
            api.get('/commandes/enCours')
                .then(response => {
                    console.log("Données reçues de l'API :", response.data);
                    this.Panier = this.transformData(response.data);
                    this.saveToLocalStorage();
                    console.log("Données après transformation :", this.Panier);
                })
                .catch(error => {
                    if (useAuthStore().getAccessToken()!=null){
                        console.error("Erreur lors de la récupération de la commande :", error);
                    }
                });
        },
        saveToLocalStorage() {
            localStorage.setItem('panier', JSON.stringify(this.$state));
        },
        loadFromLocalStorage() {
            const data = localStorage.getItem('panier');
            if (data) {
                this.$patch(JSON.parse(data));
            }
        },
        transformData(data) {
            const transformed = [];
            if(data==null || data.panier==null){
                return transformed;
            }

            data.panier.forEach(item => {
                // Trie les ingrédients par ID pour garantir la comparaison
                const sortedIngredients = item.ingredients
                    .map(ing => ({
                        id: ing.id,
                        nom: ing.nom,
                        description: ing.description,
                        prix: parseFloat(ing.prix),
                    }))
                    .sort((a, b) => a.id - b.id);

                // Vérifie si la pizza existe déjà dans le panier transformé
                const existingItem = transformed.find(cartItem =>
                    cartItem.pizza.nom === item.pizza.nom &&
                    JSON.stringify(cartItem.ingredients) === JSON.stringify(sortedIngredients)
                );

                if (existingItem) {
                    // Si la pizza existe déjà, on augmente la quantité
                    existingItem.quantity += 1;
                } else {
                    // Sinon, on crée une nouvelle entrée avec quantité = 1
                    transformed.push({
                        pizzaId: item.pizza.id,
                        pizza: {
                            id: item.pizza.id,
                            nom: item.pizza.nom,
                            description: item.pizza.description,
                            photo: item.pizza.photo,
                        },
                        ingredients: sortedIngredients,
                        ingredientsIds: sortedIngredients.map(ing => ing.id),
                        prix: sortedIngredients.reduce((total, ing) => total + parseFloat(ing.prix), 0),
                        cartItemId: Date.now(),
                        quantity: 1,
                    });
                }
            });

            return transformed;
        },
        reverseTransformData(panier) {
            const reversed = [];

            panier.forEach(item => {
                for (let i = 0; i < item.quantity; i++) {
                    reversed.push({
                        pizzaId: item.pizza.id,
                        ingredientsIds: item.ingredients.map(ing => ing.id),
                        pizza: {
                            id: item.pizza.id,
                            nom: item.pizza.nom,
                            description: item.pizza.description,
                            photo: item.pizza.photo,
                        },
                        ingredients: item.ingredients.map(ing => ({
                            id: ing.id,
                            nom: ing.nom,
                            description: ing.description,
                            prix: parseFloat(ing.prix),
                        })),
                    });
                }
            });

            return reversed ;
        },
    }
});
