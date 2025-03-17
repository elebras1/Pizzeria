<template>
  <div>
    <h1>Commande</h1>
  </div>
  <div v-for="commande in commandes" :key="commande.id">
    <h2>{{commande.id}}</h2>
    <p>{{commande.enCours}}</p>
    <div v-for="commentaire in commande.commentaires" :key="commentaire.id">
      <h3>{{commentaire.texte}}</h3>
      <img :src="commentaire.photo" alt="photo" />
    </div>

    <div v-for="pizza in commande.panier" :key="pizza.id">
      <h3>{{pizza.pizza.nom}}</h3>
      <p>{{pizza.pizza.description}}</p>
      <img :src="pizza.pizza.photo" alt="pizza.pizza.nom" />
      <div v-for="ingredient in pizza.ingredients" :key="ingredient.id">
        <h4>{{ingredient.nom}}</h4>
        <p>{{ingredient.description}}</p>
        <p>{{ingredient.prix}}</p>
      </div>
    </div>
  </div>
</template>


<script>
import api from "@/interceptors/api.js";

export default {
  name: "Commande",
  data() {
    return {
      commande:{
        enCours: true,
        commentaires: [
          {
            id: 1,
            texte: "bien",
            photo: "vhj",
          }
        ],
        panier: [
          {
            id: 1,
            pizza:{
              id: 1,
              nom: "Margherita",
              description: "dfs",
              photo: "",
            },
            ingredients:[
              {
                id:1,
                nom:"tomate",
                description: "Ingredient",
                prix: 1.5,
              }
            ]
          }
        ],
      },
      commandes: [],
    }
  },
  methods: {
    getCommandes() {
      this.commandes.push(this.commande)
      /*
      api.get('http://localhost:3000/commandes')
        .then(response => {
          this.commandes = response.data;
        })
        .catch(e => {
          console.log(e);
        });

       */
    },
  },
  mounted() {
    this.getCommandes();
  },
}
</script>


<style scoped>

</style>