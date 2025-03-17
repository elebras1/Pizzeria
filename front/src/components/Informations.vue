<script setup>
import api from "@/interceptors/api.js";
import { ref } from "vue";

const account = ref({
  pseudo: "",
  nom: "",
  prenom: ""
});
const editMode = ref(false);
const changePasswordMode = ref(false);
const currentPassword = ref("");
const newPassword = ref("");
const confirmPassword = ref("");

const fetchAccount = async () => {
  try {
    const response = await api.get("/comptes");
    account.value = response.data;
  } catch (error) {
    console.error("Erreur lors du chargement du compte :", error);
  }
};

const updateAccount = async () => {
  try {
    await api.put("/comptes", {
      nom: account.value.nom,
      prenom: account.value.prenom
    });
    alert("Informations mises à jour avec succès !");
    editMode.value = false;
  } catch (error) {
    console.error("Erreur lors de la mise à jour du compte :", error);
    alert("Une erreur est survenue lors de la mise à jour.");
  }
};

const changePassword = async () => {
  if (newPassword.value !== confirmPassword.value) {
    alert("Les mots de passe ne correspondent pas.");
    return;
  }

  try {
    await api.put("/comptes/password", {
      currentPassword: currentPassword.value,
      newPassword: newPassword.value
    });
    alert("Mot de passe mis à jour avec succès !");
    changePasswordMode.value = false;
    currentPassword.value = "";
    newPassword.value = "";
    confirmPassword.value = "";
  } catch (error) {
    console.error("Erreur lors du changement de mot de passe :", error);
    alert("Une erreur est survenue lors du changement de mot de passe.");
  }
};

fetchAccount();
</script>

<template>
  <div>
    <h1>Compte</h1>
    <div v-if="account">
      <p><strong>Pseudo :</strong> {{ account.pseudo }}</p>

      <div v-if="editMode">
        <label for="nom">Nom :</label>
        <input
            id="nom"
            v-model="account.nom"
            type="text"
            placeholder="Nom"
        />

        <label for="prenom">Prénom :</label>
        <input
            id="prenom"
            v-model="account.prenom"
            type="text"
            placeholder="Prénom"
        />

        <button @click="updateAccount">Enregistrer</button>
        <button @click="editMode = false">Annuler</button>
      </div>

      <div v-else>
        <p><strong>Nom :</strong> {{ account.nom }}</p>
        <p><strong>Prénom :</strong> {{ account.prenom }}</p>
        <button @click="editMode = true">Modifier</button>
        <button @click="changePasswordMode = true">Changer le mot de passe</button>
      </div>

      <div v-if="changePasswordMode" class="password-change">
        <h2>Changer le mot de passe</h2>
        <label for="current-password">Mot de passe actuel :</label>
        <input
            id="current-password"
            v-model="currentPassword"
            type="password"
            placeholder="Mot de passe actuel"
        />

        <label for="new-password">Nouveau mot de passe :</label>
        <input
            id="new-password"
            v-model="newPassword"
            type="password"
            placeholder="Nouveau mot de passe"
        />

        <label for="confirm-password">Confirmer le mot de passe :</label>
        <input
            id="confirm-password"
            v-model="confirmPassword"
            type="password"
            placeholder="Confirmer le mot de passe"
        />

        <button @click="changePassword">Valider</button>
        <button @click="changePasswordMode = false">Annuler</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
h1 {
  margin-bottom: 10px;
}

label {
  display: block;
  margin: 5px 0;
}

input {
  padding: 5px;
  margin-bottom: 10px;
  width: 100%;
  max-width: 300px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  margin: 5px;
  padding: 5px 10px;
  border-radius: 4px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

button:last-child {
  background-color: #dc3545;
}

button:last-child:hover {
  background-color: #c82333;
}

.password-change {
  margin-top: 20px;
}

.password-change h2 {
  margin-bottom: 10px;
}
</style>
