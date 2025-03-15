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
                if (this.Panier.length === 0) {
                    this.Panier.push(pizza);
                    const commande={
                        compteId : null,
                        enCours : true,
                        panier : this.Panier,
                        commentairesIds : null
                    }
                    console.log(JSON.stringify(commande));
                    const response = await api.post('/commandes',commande , {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });
                    console.log(response.data);
                    this.setPanier(response.data);
                    console.log('Pizza ajoutée via POST:', response.data);
                } else {
                    const updatedPanier = [...this.Panier, pizza];
                    const response = await api.put('/commandes', {
                        compteId : 0,
                        enCours : true,
                        panier : updatedPanier,
                        commentairesIds : [""]
                    }, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });
                    this.setPanier(response.data);
                    console.log('Pizza ajoutée via UPDATE:', response.data);
                }
            } catch (error) {
                console.error('Erreur lors de l\'ajout de la pizza:', error);
            }
        },

    }
});
