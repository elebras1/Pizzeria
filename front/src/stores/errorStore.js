// stores/errorStore.js
import { defineStore } from 'pinia';

export const useErrorStore = defineStore('error', {
    state: () => ({
        errorMessage: null,
        showModal: false,
    }),
    actions: {
        setError(message) {
            this.errorMessage = message;
            this.showModal = true;  // Ouvrir la modal automatiquement
        },
        clearError() {
            this.errorMessage = null;
            this.showModal = false;
        },
    },
});
