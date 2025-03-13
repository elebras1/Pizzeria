<template>
  <div class="login-form">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <label for="pseudo">Pseudo</label>
      <input v-model="pseudo" type="text" id="pseudo" placeholder="Pseudo" required />

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

const pseudo = ref('');
const password = ref('');
const router = useRouter();

const handleLogin = () => {
  alert(`Login avec pseudo: ${pseudo.value}, mot de passe: ${password.value}`);
};

const goToRegister = () => {
  router.push('/register');
};
</script>
