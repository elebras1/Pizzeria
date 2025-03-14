import { defineStore } from 'pinia';
import api from '@/interceptors/api.js';

export const useAuthStore = defineStore('auth', {
    state: () => ({
        accessToken: null,
        isLoggedIn: false,
    }),

    actions: {
        async login(username, password) {
            try {
                const response = await api.post('/auth/login', { username, password });
                this.accessToken = response.data.accessToken;
                this.isLoggedIn = true;
                api.defaults.headers.common['Authorization'] = `Bearer ${this.accessToken}`;
                return true;
            } catch (error) {
                console.error('Erreur de connexion:', error);
                return false;
            }
        },

        async logout() {
            try {
                await api.post("/auth/logout");
                this.isLoggedIn = false;
                this.accessToken = null;
            } catch (error) {
                console.error("Erreur lors de la déconnexion", error);
            }
        },
    }
});
