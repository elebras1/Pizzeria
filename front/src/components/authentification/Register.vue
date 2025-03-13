
<!-- RegisterForm.vue -->
<template>
  <div class="register-form">
    <h2>Register</h2>
    <form @submit.prevent="handleRegister">
      <label for="pseudoRegister">Pseudo</label>
      <input v-model="pseudoRegister" type="text" id="pseudoRegister" placeholder="Pseudo" required />

      <label for="nom">Nom</label>
      <input v-model="nom" type="text" id="nom" placeholder="Nom" required />

      <label for="prenom">Prénom</label>
      <input v-model="prenom" type="text" id="prenom" placeholder="Prénom" required />

      <label for="passwordRegister">Mot de passe</label>
      <input v-model="passwordRegister" type="password" id="passwordRegister" placeholder="Mot de passe" required />

      <label for="confirmPassword">Confirmer le mot de passe</label>
      <input v-model="confirmPassword" type="password" id="confirmPassword" placeholder="Confirmer le mot de passe" required />

      <button type="submit">S'inscrire</button>
      <button type="button" @click="goToLogin">Déjà inscrit ?</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const pseudoRegister = ref('');
const nom = ref('');
const prenom = ref('');
const passwordRegister = ref('');
const confirmPassword = ref('');
const router = useRouter();

const handleRegister = () => {
  if (passwordRegister.value !== confirmPassword.value) {
    alert("Les mots de passe ne correspondent pas !");
    return;
  }
  alert(`Inscription avec pseudo: ${pseudoRegister.value}, nom: ${nom.value}, prénom: ${prenom.value}`);
};

const goToLogin = () => {
  router.push('/');
};
</script>
