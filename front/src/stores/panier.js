import { defineStore } from 'pinia';

export const usePanierStore = defineStore('panier', {
    state: () => ({
        Panier: []
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
        saveToLocalStorage() {
            localStorage.setItem('panier', JSON.stringify(this.$state));
        },
        loadFromLocalStorage() {
            const data = localStorage.getItem('panier');
            if (data) {
                this.$patch(JSON.parse(data));
            }
        },
    }
});
