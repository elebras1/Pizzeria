import { defineStore } from 'pinia';
import api from '@/interceptors/api.js';
import axios from "axios";

export const usePanierStore = defineStore('panier', {
    state: () => ({
        Panier: []
    }),
    actions: {
        setPanier(data) {
            this.Panier = data;
        },
        async addPizza(pizza) {
            try {

            } catch (error) {
                console.error('Erreur lors de l\'ajout de la pizza:', error);
            }
        },
        async removePizza(pizza) {
            try {

            } catch (error) {
                console.error('Erreur lors de la suppression de la pizza:', error);
            }
        },
        async clearPanier() {
            try {

            } catch (error) {
                console.error('Erreur lors de la suppression du panier:', error);
            }
        },
        async getPanier() {
            try {

            } catch (error) {
                console.error('Erreur lors de la récupération du panier:', error);
            }
        }
    }
});
