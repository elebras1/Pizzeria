
<template>

  <div>
    <h1>Panier</h1>
    <div v-for="pizza in panier" :key="pizza.id">
      <h2>{{ pizza.nom }}</h2>
      <p>{{ pizza.prix }} €</p>
      <ul>
        <li v-for="ing in pizza.ingredients" :key="ing.id">
          {{ ing.nom }} ({{ ing.prix }} €)
        </li>
      </ul>
    </div>
  </div>
</template>

<script>

import { usePanierStore } from '@/stores/panier';

export default {
  name: "Panier",
  data() {
    return {
      panier: [],

    };
  },
  methods: {
    async getPanier() {
      this.panier = usePanierStore().Panier;
    },
  },
  mounted() {
    this.getPanier();
  },
};
</script>


<style scoped>

</style>