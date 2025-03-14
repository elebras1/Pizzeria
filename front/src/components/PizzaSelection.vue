<template>
  <div class="pizza-selection">
    <img src="https://media.gettyimages.com/id/938742222/fr/photo/pizza-pepperoni-fromage.jpg?s=612x612&w=gi&k=20&c=iASmsm5ZD7sk79jE4eQNy0dACaM-cLr7eUIYEPRxFdQ=" alt="Extra Cheddar Mac & Cheese" class="pizza-image" />
    <h2>{{ Pizza.nom }}</h2>
    <h2>Ingrédients Standards</h2>
    <div v-for="ingrediant in Pizza.standardIngredients" :key="ingrediant.id" class="ingredients">
      <div class="ingredient">
        <label>{{ingrediant.nom}}</label>
        <input v-if="ingrediant.nom==='pate'" type="checkbox" checked disabled/>
        <input v-else type="checkbox" checked/>
      </div>
    </div>
    <h2>Ingrédients Optionnels</h2>
    <div v-for="ingrediant in Pizza.optionalIngredients" :key="ingrediant.id" class="ingredients">
      <div class="ingredient">
        <div>
          <label>{{ingrediant.nom}}</label>
          <label class="price">+{{ingrediant.prix}}€</label>
        </div>
        <input
            type="checkbox"
            :value="ingrediant.id"
            @change="toggleIngredient(ingrediant.id)"
        />
      </div>
    </div>
    <button @click="addToCart">{{ Price }} € Ajouter au panier</button>
  </div>
</template>


<script>
export default {
  name: "PizzaSelection",
  data() {
    return {
      Price: 0,
      selectedOptionalIngredients: [],
      Pizza: {
        id: null,
        nom: "gdfsg",
        description: "",
        photo: "",
        standardIngredients: [
          { id: 1, nom: "Sauce tomate", prix: 0.5 },
          { id: 2, nom: "pate", prix: 0.5 },
          { id: 3, nom: "Basilic", prix: 0.5 },
        ],
        optionalIngredients: [
          { id: 4, nom: "Jambon", prix: 1 },
          { id: 5, nom: "Champignons", prix: 2.5 },
          { id: 6, nom: "Gorgonzola", prix: 1.75 },
        ],
      },
    };
  },
  methods: {
    calculatePrice() {
      let total = this.Pizza.standardIngredients.reduce((acc, ing) => acc + ing.prix, 0);
      total += this.selectedOptionalIngredients.reduce((acc, id) => {
        const ingredient = this.Pizza.optionalIngredients.find((ing) => ing.id === id);
        return ingredient ? acc + ingredient.prix : acc;
      }, 0);
      this.Price =  total.toFixed(2);
    },
    toggleIngredient(id) {
      if (this.selectedOptionalIngredients.includes(id)) {
        this.selectedOptionalIngredients = this.selectedOptionalIngredients.filter((i) => i !== id);
      } else {
        this.selectedOptionalIngredients.push(id);
      }
      this.calculatePrice();
    },
    addToCart() {
      console.log("Ajouté au panier avec un prix de " + this.Price + " €");
    },
  },
  mounted() {
    this.calculatePrice();
  },
};
</script>

<style scoped>
.pizza-selection {
  margin: 20px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
}
button {
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
p {
  color: black;
}
.ingredient{
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  margin: 0.5rem;
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
  top: 7px;
  left:7px;
  width: 6px;
  height: 6px;
  background-color: white;
  border-radius: 50%;
}
.price{
  color: #7c7c7c;
  font-weight: normal;
}
</style>
