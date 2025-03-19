<template>
    <div class="dashboard">
      <div class="card" @click="navigate('PizzaList')">
        <h2>Pizzas</h2>
      </div>
      <div class="card" @click="navigate('IngredientList')">
        <h2>Ingrédients</h2>
      </div>
      <div class="card" @click="navigate('CommandeListAll')">
        <h2>Commandes</h2>
      </div>
      <div class="card" @click="navigate('CompteList')">
        <h2>Comptes</h2>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
  
  const navigate = (routeName: string) => {
    router.push({ name: routeName });
  };
  </script>
  
  <style scoped>
  .dashboard {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    justify-content: center;
    align-items: center;
    margin: 50px;
  }
  
  .card {
    width: 250px;
    height: 150px;
    background-color: #007bff;
    color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    cursor: pointer;
    transition: transform 0.2s ease, box-shadow 0.2s ease;
  }
  
  .card:hover {
    transform: scale(1.05);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
  }
  </style>
  