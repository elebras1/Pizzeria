<script setup>
import { ref, onMounted } from 'vue';
import { useAuthStore } from '../stores/auth';
import axios from "axios";
import PizzaSelection from "./PizzaSelection.vue";
import ModalConnexion from "./ModalConnexion.vue";

const authStore = useAuthStore();
const pizzas = ref([]);
const showPizzaModal = ref(false);
const showLoginModal = ref(false);
const selectedPizza = ref(null);

onMounted(() => {
  getAllPizzas();
  authStore.initialize();
});

const getAllPizzas = async () => {
  try {
    const response = await axios.get("http://localhost:3000/api/pizzas");
    pizzas.value = response.data;
  } catch (error) {
    console.error(error);
  }
};

const openPizzaModal = (pizza) => {
  if (authStore.isLoggedIn) {
    selectedPizza.value = pizza;
    showPizzaModal.value = true;
  } else {
    showLoginModal.value = true;
  }
};

const closePizzaModal = () => {
  showPizzaModal.value = false;
};

const closeLoginModal = () => {
  showLoginModal.value = false;
};

const calculatePrice = (pizza) => {
  const standardPrice = pizza.standardIngredients.reduce((acc, ingredient) => acc + ingredient.prix, 0);
  return standardPrice.toFixed(2);
};
</script>

<template>
  <div>
    <h2>Nos délicieuses pizzas</h2>
    <div class="pizza-list">
      <div v-for="pizza in pizzas" :key="pizza.id" class="pizza-item" @click="openPizzaModal(pizza)">
        <div class="pizza-info">
          <h3>{{ pizza.nom }}</h3>
          <p class="pizza-price">{{ calculatePrice(pizza) }} €</p>
          <p>{{ pizza.description }}</p>
          <span v-if="pizza.popular" class="label">Populaire</span>
        </div>
        <img :src="pizza.image" :alt="pizza.name" class="pizza-image" />
      </div>
    </div>

    <!-- Modal de Sélection de Pizza -->
    <PizzaSelection v-if="showPizzaModal && authStore.isLoggedIn" :pizza="selectedPizza" :isVisible="showPizzaModal"
      @close="closePizzaModal" />

    <!-- Modal de Connexion -->
    <ModalConnexion v-if="showLoginModal && !authStore.isLoggedIn" :isVisible="showLoginModal"
      @close="closeLoginModal" />
  </div>
</template>

<style scoped>
.pizza-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin: 1rem;
}

.pizza-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  cursor: pointer;
}

.pizza-info {
  flex: 1;
  margin-right: 1rem;
}

.pizza-image {
  width: 150px;
  height: 150px;
  border-radius: 8px;
}

.pizza-price {
  font-weight: bold;
  margin: 0.5rem 0;
}

.label {
  background-color: #28a745;
  color: white;
  padding: 0.2rem 0.5rem;
  border-radius: 5px;
  font-size: 0.8rem;
}

h2 {
  text-align: center;
  margin-top: 1rem;
  color: black;
}

h3,
h4,
h5,
p {
  color: black;
}
</style>
