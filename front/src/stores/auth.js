import { defineStore } from 'pinia';
import api from '@/interceptors/api.js';

export const useAuthStore = defineStore('auth', {
    state: () => ({
        accessToken: localStorage.getItem('accessToken') || null,
        isLoggedIn: !!localStorage.getItem('accessToken'),
    }),

    actions: {
        initialize() {
            if (this.accessToken) {
                api.defaults.headers.common['Authorization'] = `Bearer ${this.accessToken}`;
            }
        },
        async login(username, password) {
            try {
                const response = await api.post('/auth/login', { username, password });
                this.accessToken = response.data.accessToken;
                this.isLoggedIn = true;
                localStorage.setItem('accessToken', this.accessToken);
                api.defaults.headers.common['Authorization'] = `Bearer ${this.accessToken}`;
                console.log(response.data);
                return true;
            } catch (error) {
                console.error('Erreur de connexion:', error);
                return false;
            }
        },

        async logout() {
            try {
                await api.post("/auth/logout").then(response => {
                    console.log(response.data);
                });
                this.clearAuth();
            } catch (error) {
                console.error("Erreur lors de la déconnexion", error);
            }
        },

        clearAuth() {
            this.isLoggedIn = false;
            this.accessToken = null;
            localStorage.removeItem('accessToken');
            delete api.defaults.headers.common['Authorization'];
        }
    }
});
