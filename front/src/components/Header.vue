<template>
  <header class="navbar">
    <div class="logo">
      <p>
        <a href="/">PizzaYolo</a>
      </p>
    </div>
    <div class="auth">
      <button v-if="!isLoggedIn" @click="handleLogin">Connexion</button>
      <button v-else @click="handleLogout">Déconnexion</button>
    </div>
  </header>
</template>

<script>
import { useAuthStore } from '@/stores/auth';
import router from "@/router/index.js";

export default {
  name: 'Header',
  computed: {
    isLoggedIn() {
      return useAuthStore().isLoggedIn;
    }
  },
  methods: {
    handleLogin() {
      router.push('/login');
    },
    handleLogout() {
      useAuthStore().logout();
    }
  }
};
</script>


<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  padding-left: 1em;
}

.logo > p {
  color: black;
}

.auth {
  padding-right: 1em;
}

.auth button {
  background-color: #555;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
}

.auth button:hover {
  background-color: #777;
}
</style>
