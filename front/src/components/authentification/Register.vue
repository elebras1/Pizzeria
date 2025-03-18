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
      <button type="button" @click="goToLogin" class="login-button">Déjà inscrit ?</button>
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
  router.push('/login');
};
</script>

<style scoped>
.register-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
}

.register-form h2 {
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

.login-button {
  background-color: #eee;
  color: #333;
  margin-top: 10px;
}

.login-button:hover {
  background-color: #ddd;
}
</style>
