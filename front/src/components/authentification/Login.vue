<template>
  <div class="login-form">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <label for="username">Pseudo</label>
      <input v-model="username" type="text" id="username" placeholder="Pseudo" required />

      <label for="password">Mot de passe</label>
      <input v-model="password" type="password" id="password" placeholder="Mot de passe" required />

      <button type="submit">Se connecter</button>
      <button type="button" @click="goToRegister" class="register-button">Pas encore inscrit ?</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth.js';
import { usePanierStore } from "@/stores/panier.js";

const authStore = useAuthStore();
const panierStore = usePanierStore();
const username = ref('');
const password = ref('');
const router = useRouter();

const handleLogin = async () => {
  const success = await authStore.login(username.value, password.value);
  if (success) {
    panierStore.loadFromLocalStorage();
    await router.push('/');
  } else {
    console.error("Échec de la connexion");
  }
};

const goToRegister = () => {
  router.push('/register');
};
</script>

<style scoped>
.login-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
}

.login-form h2 {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

form {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 300px;
}

label {
  margin-bottom: 5px;
  font-weight: bold;
  color: #555;
}

input[type="text"],
input[type="password"] {
  width: 100%;
  padding: 8px;
  margin: 8px 0 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

button {
  width: 100%;
  padding: 10px;
  margin-top: 8px;
  background-color: #4285f4;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #357ae8;
}

.register-button {
  background-color: #eee;
  color: #333;
  margin-top: 10px;
}

.register-button:hover {
  background-color: #ddd;
}
</style>
