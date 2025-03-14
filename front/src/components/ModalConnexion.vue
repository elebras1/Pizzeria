<template>
  <div v-if="isVisible" class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <h3>Connexion requise</h3>
      <p>Vous devez être connecté pour commander une pizza.</p>
      <button @click="redirectToLogin">Se connecter</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "ModalConnexion",
  props: ["isVisible"],
  methods: {
    closeModal() {
      this.$emit("close");
    },
    redirectToLogin() {
      window.location.href = "/login";
    },
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
  text-align: center;
}

.close-button {
  position: absolute;
  top: 5px;
  right: 5px;
  background: red;
  color: white;
  border: none;
  padding: 5px;
  border-radius: 3px;
  cursor: pointer;
}

button {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  margin-top: 1rem;
  border-radius: 5px;
  cursor: pointer;
}
</style>
