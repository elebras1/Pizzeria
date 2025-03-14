<template>
  <div class="login-form">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <label for="username">Pseudo</label>
      <input v-model="username" type="text" id="username" placeholder="Pseudo" required />

      <label for="password">Mot de passe</label>
      <input v-model="password" type="password" id="password" placeholder="Mot de passe" required />

      <button type="submit">Se connecter</button>
      <button type="button" @click="goToRegister">Pas encore inscrit ?</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from "axios";
import { useAuthStore } from '@/stores/auth.js';

const authStore = useAuthStore();
const username = ref('');
const password = ref('');
const router = useRouter();

const handleLogin = () => {
  axios.post('http://localhost:3000/api/auth/login', {
    username: username.value,
    password: password.value,
  })
    .then((response) => {
      if (response.data.accessToken) {
        localStorage.setItem("accessToken", response.data.accessToken);
        authStore.login(); // Mettre à jour l'état global
        router.push('/'); // Redirection vers la page d'accueil
      }
    })
    .catch((error) => {
      console.error(error);
    });
};

const goToRegister = () => {
  router.push('/register');
};
</script>
