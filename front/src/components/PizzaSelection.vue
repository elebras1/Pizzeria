<template>
  <div v-if="isVisible" class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <div class="pizza-selection">
        <img :src="Pizza.image" alt="Pizza Image" class="pizza-image" />
        <h2>{{ Pizza.nom }}</h2>
        <h3>Ingrédients Standards</h3>
        <div v-for="ingrediant in Pizza.standardIngredients" :key="ingrediant.id" class="ingredient">
          <label>{{ ingrediant.nom }}</label>
          <input v-if="ingrediant.nom === 'pate'" type="checkbox" checked disabled />
          <input v-else type="checkbox" checked
                 :value="ingrediant.id"
                 @change="toggleIngredient(ingrediant.id)"
          />
        </div>
        <h3>Ingrédients Optionnels</h3>
        <div v-for="ingredient in Pizza.optionalIngredients" :key="ingredient.id" class="ingredient">
          <div class="ingredient-info">
            <label>{{ ingredient.nom }}</label>
            <label class="price">+{{ ingredient.prix }} €</label>
          </div>
          <input
              type="checkbox"
              :value="ingredient.id"
              @change="toggleIngredient(ingredient.id)"
          />
        </div>
        <button @click="addToCart">{{ Price }} € - Ajouter au panier</button>
      </div>
    </div>
  </div>
</template>

<script>
import { usePanierStore } from '@/stores/panier';

export default {
  name: "PizzaSelection",
  props: ["pizza", "isVisible"],
  data() {
    return {
      Price: 0,
      selectedIngredients: [],
      Pizza: {
        nom: "",
        image: "",
        standardIngredients: [],
        optionalIngredients: [],
      },
      panier: [],
    };
  },
  methods: {
    async calculatePrice() {
      let total = this.selectedIngredients.reduce((acc, ing) => acc + ing.prix, 0);
      this.Price = total.toFixed(2);
    },
    toggleIngredient(id) {
      const index = this.selectedIngredients.indexOf(id);
      if (index !== -1) {
        this.selectedIngredients.splice(index, 1);
      } else {
        this.selectedIngredients.push(id);
      }
      this.calculatePrice();
    },
    addToCart() {
      console.log("Ajouté au panier avec un prix de " + this.Price + " €");
      this.panier = {
        pizzaId: this.pizza.id,
        ingredientsIds: this.selectedIngredients,
      };
      usePanierStore().addPizza(this.panier);
    },
    closeModal() {
      this.$emit("close");
    },
    getPizza() {
      this.Pizza = this.pizza;
      this.selectedIngredients = this.Pizza.standardIngredients.map((ing) => ing.id);
      this.calculatePrice();
    },

  },
  mounted() {
    this.getPizza();
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
  position: relative;
}

.pizza-image {
  width: 100%;
  height: auto;
  margin: 10px 0;
}

.ingredient {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem;
  margin-top: 0.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

input[type="checkbox"] {
  appearance: none;
  -webkit-appearance: none;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  border: 2px solid #333;
  background-color: white;
  margin: 5px;
  outline: none;
  cursor: pointer;
}

input[type="checkbox"]:checked {
  background-color: black;
  position: relative;
}

input[type="checkbox"]:checked::after {
  content: "";
  position: absolute;
  top: 5px;
  left: 5px;
  width: 6px;
  height: 6px;
  background-color: white;
  border-radius: 50%;
}

.price {
  display: block;
  margin-top: 4px;
  color: #7c7c7c;
  font-weight: normal;
}

button {
  width: 100%;
  height: 40px;
  margin-top: 10px;
  padding: 8px 16px;
  background-color: #000000;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background-color: #45a049;
}
</style>
