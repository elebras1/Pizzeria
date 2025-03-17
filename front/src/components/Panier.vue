<template>
  <div>
    <button @click="loadPanier">Load Panier</button>
    <button @click="savePanier">Save Panier</button>
  </div>
  <div class="panier-container">
    <h1>Panier</h1>
    <div v-for="(pizza, index) in panier" :key="index" class="pizza-card">
      <div class="quantity-controls">
        <button class="quantity-btn" @click="decreaseQuantity(pizza)">−</button>
        <button class="quantity-btn" @click="increaseQuantity(pizza)">+</button>
      </div>
      <h2>{{ pizza.pizza.nom }} x {{ pizza.quantity }}</h2>
      <ul class="ingredients-list">
        <li v-for="ing in pizza.ingredients" :key="ing.id" class="ingredient-item">
          <span>{{ ing.nom }}</span>
          <span class="ingredient-price">{{ ing.prix.toFixed(2) }} €</span>
        </li>
        <li class="ingredient-price">
          Total : {{ pizzaPrice(pizza) }} €
        </li>
      </ul>
    </div>
    <div class="total">
      <div v-for="(pizza, index) in panier" :key="index">
        <p>{{ pizza.pizza.nom }} - {{ pizzaPrice(pizza) }} € x {{ pizza.quantity }} = {{ pizzaPrice(pizza) }}</p>
      </div>
      <h2>Total : {{ totalPrice.toFixed(2) }} €</h2>
    </div>
    <button class="pay-btn" @click="pay">Payer</button>
  </div>
</template>

<script>
import { usePanierStore } from '@/stores/panier';
import { useAuthStore } from '@/stores/auth.js';

export default {
  name: "Panier",
  data() {
    return {
      commande : {
        compteId: null,
        enCours: true,
        panier: [],
        commentairesIds: [],
      }
    };
  },
  computed: {
    totalPrice() {
      return this.panier.reduce((acc, pizza) => acc + pizza.prix * pizza.quantity, 0);
    },
    panier() {
      return usePanierStore().getPanier();
    },
  },
  methods: {
    removePizza(pizza) {
      usePanierStore().removePizza(pizza);
    },
    pizzaPrice(pizza) {
      return (pizza.prix * pizza.quantity).toFixed(2);
    },
    increaseQuantity(pizza) {
      pizza.quantity += 1;
      usePanierStore().saveToLocalStorage();
    },
    decreaseQuantity(pizza) {
      if (pizza.quantity > 1) {
        pizza.quantity -= 1;
        usePanierStore().saveToLocalStorage();
      } else {
        this.removePizza(pizza);
      }
    },
    loadPanier() {
      usePanierStore().loadPanier();
    },
    savePanier(){
      usePanierStore().savePanier();
    },
    pay() {

    },
  },
  mounted() {
    useAuthStore().initialize();
    usePanierStore().loadFromLocalStorage();

  },
};
</script>

<style scoped>
.total{
  text-align: right;
}
.panier-container {
  max-width: 600px;
  margin: auto;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}

.pizza-card {
  position: relative;
  background-color: #fff;
  margin: 10px 0;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.quantity-controls {
  position: absolute;
  top: 8px;
  right: 8px;
  display: flex;
  align-items: center;
}

.quantity-btn {
  background-color: #000000;
  color: white;
  border: none;
  padding: 4px 8px;
  margin: 0 2px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.2s;
}

.quantity-btn:hover {
  background-color: #424242;
}

.ingredients-list {
  list-style-type: none;
  padding-top: 0.5em;
  padding-left: 1em;
  margin: 10px 0;
}
.ingredient-item {
  display: flex;
  justify-content: space-between;
  margin: 3px 0;
}

.ingredient-price {
  font-weight: bold;
  min-width: 60px;
  text-align: right;
}


.pay-btn {
  display: block;
  margin: 20px auto;
  width: 100%;
  font-size: 16px;
}
.pay-btn{
  background-color: #000000;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  margin-top: 10px;
  cursor: pointer;
  transition: background-color 0.2s;
}
.pay-btn:hover {
  background-color: #28a745;
}
</style>
