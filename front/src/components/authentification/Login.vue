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
import { useAuthStore } from '@/stores/auth.js';

const authStore = useAuthStore();
const username = ref('');
const password = ref('');
const router = useRouter();

const handleLogin = async () => {
  const success = await authStore.login(username.value, password.value);
  if (success) {
    await router.push('/');
  } else {
    console.error("Échec de la connexion");
  }
};

const goToRegister = () => {
  router.push('/register');
};
</script>
