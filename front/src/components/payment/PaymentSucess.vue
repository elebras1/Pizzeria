<script setup>
import { useRouter } from "vue-router";
import api from "@/interceptors/api.js";
import {onMounted} from "vue";
import { usePanierStore} from "@/stores/panier.js";

onMounted(() => {
  pay();
});
const pay = async () => {
  await api.put("/commandes/paySuccess")
  usePanierStore().clearPanier();
}

</script>
<script>
export default {
  name: "PaymentSuccess",
  data() {
    return {
      message: "Le paiement a été effectué avec succès !",
    };
  },
};
</script>

<template>
  <div class="payment-success">
    <h1>{{ message }}</h1>
    <p>Merci pour votre commande ! Votre paiement a été validé.</p>
    <router-link to="/" class="btn">Retour à l'accueil</router-link>
  </div>
</template>

<style scoped>
.payment-success {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  text-align: center;
}

h1 {
  color: #4caf50;
  font-size: 2rem;
  margin-bottom: 1rem;
}

p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
}

.btn {
  background-color: #4caf50;
  color: white;
  padding: 0.5rem 1.5rem;
  border-radius: 8px;
  text-decoration: none;
  transition: background-color 0.3s ease;
}

.btn:hover {
  background-color: #43a047;
}
</style>
