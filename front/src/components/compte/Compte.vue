<template>
  <div class="dashboard">
    <div class="card" @click="toggleInformations">
      <h2>Mes Informations</h2>
    </div>
    <div class="card" @click="toggleCommande">
      <h2>Mes Commande</h2>
    </div>

    <!-- Modale Informations -->
    <div v-if="showInformations" class="modal" @click.self="toggleInformations">
      <div class="modal-content">
        <Informations></Informations>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import Informations from "@/components/compte/Informations.vue";
import Commande from "@/components/commande/Commande.vue";
import { useRouter } from "vue-router";

const showInformations = ref(false);
const showCommande = ref(false);
const router = useRouter();

const toggleInformations = () => {
  showInformations.value = !showInformations.value;
};

const toggleCommande = () => {
  router.push({ path: "/account/commandes" });
};
</script>

<style scoped>
.dashboard {
  display: flex;
  gap: 20px;
  justify-content: center;
  align-items: center;
  margin: 50px;
}

.card {
  width: 200px;
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

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  min-width: 300px;
  max-width: 600px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
}

.close {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 5px 10px;
  margin-bottom: 10px;
  cursor: pointer;
  border-radius: 4px;
}

.close:hover {
  background-color: #c82333;
}
</style>
