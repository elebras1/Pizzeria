<template>
  <div>
    <h2>Nos délicieuses pizzas</h2>
    <div class="pizza-list">
      <div v-for="pizza in pizzas" :key="pizza.id" class="pizza-item" @click="jsp">
        <div class="pizza-info">
          <h3>{{ pizza.name }}</h3>
          <p class="pizza-price">{{ pizza.price }} €</p>
          <p>{{ pizza.description }}</p>
          <span v-if="pizza.popular" class="label">Populaire</span>
        </div>
        <img :src="pizza.image" :alt="pizza.name" class="pizza-image" />
      </div>
    </div>
  </div>
</template>

<script>
import router from "@/router/index.js";
import axios from "axios";

export default {
  name: "Home",
  data() {
    return {
      pizzas: [
        { id: 1, name: "Margherita", description: "Sauce tomate, mozzarella, basilic", price: 8.5, image: "https://media.gettyimages.com/id/938742222/fr/photo/pizza-pepperoni-fromage.jpg?s=612x612&w=gi&k=20&c=iASmsm5ZD7sk79jE4eQNy0dACaM-cLr7eUIYEPRxFdQ=", popular: true },
        { id: 2, name: "Pepperoni", description: "Sauce tomate, mozzarella, pepperoni", price: 10.0, image: "https://via.placeholder.com/300" },
        { id: 3, name: "Reine", description: "Sauce tomate, jambon, champignons, mozzarella", price: 9.5, image: "https://via.placeholder.com/300", popular: true },
        { id: 4, name: "Quatre Fromages", description: "Mozzarella, gorgonzola, parmesan, chèvre", price: 11.0, image: "https://via.placeholder.com/300" }
      ]
    };
  },
  methods: {
    async getAllPizzas() {
      try {
        const response = await axios.get("/api/pizzas");
        this.pizzas = response.data;
      } catch (error) {
        console.error(error);
      }
    },
    jsp() {
      router.push('/pizza-selection');
    }
  }
};
</script>

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
}

.pizza-info {
  flex: 1;
  margin-right: 1rem;
}

.pizza-image {
  width: 100px;
  height: 100px;
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
  color: white;
}
h3,h4,h5,p {
  color: black;
}
</style>
