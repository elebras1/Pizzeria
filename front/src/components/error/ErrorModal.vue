<template>
  <div v-if="showModal" class="modal" @click.self="clearError">
    <div class="modal-content">
      <p>{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup>
import { useErrorStore } from '@/stores/errorStore.js';
import { computed } from 'vue';

const errorStore = useErrorStore();

const errorMessage = computed(() => errorStore.errorMessage);
const showModal = computed(() => errorStore.showModal);

const clearError = () => {
  errorStore.clearError();
};
</script>

<style>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
}
</style>
