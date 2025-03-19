<template>
  <div v-if="message" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
    <div class="bg-white p-4 rounded-lg shadow-lg">
      <p>{{ message }}</p>
      <button @click="clearError" class="mt-2 bg-red-500 text-white px-3 py-1 rounded">Fermer</button>
    </div>
  </div>
</template>

<script setup>
import { useErrorStore } from '@/stores/errorStore';

const errorStore = useErrorStore();
const message = errorStore.message;

function clearError() {
  errorStore.clearError();
}
</script>

<style>
/* Ajoute un peu de style si nécessaire */
</style>
