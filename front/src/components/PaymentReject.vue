<script>
export default {
  name: "PaymentReject",
  data() {
    return {
      message: "Le paiement a été rejeté.",
    };
  },
};
</script>

<template>
  <div class="payment-reject">
    <h1>{{ message }}</h1>
    <p>Une erreur est survenue lors du traitement de votre paiement. Veuillez réessayer.</p>
    <router-link to="/panier" class="btn">Retour au panier</router-link>
  </div>
</template>

<style scoped>
.payment-reject {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  text-align: center;
}

h1 {
  color: #f44336;
  font-size: 2rem;
  margin-bottom: 1rem;
}

p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
}

.btn {
  background-color: #f44336;
  color: white;
  padding: 0.5rem 1.5rem;
  border-radius: 8px;
  text-decoration: none;
  transition: background-color 0.3s ease;
}

.btn:hover {
  background-color: #d32f2f;
}
</style>
