import { defineStore } from 'pinia';

export const useErrorStore = defineStore('error', {
    state: () => ({
        message: null,
    }),
    actions: {
        setError(message) {
            this.message = message;
        },
        clearError() {
            this.message = null;
        },
    },
});
