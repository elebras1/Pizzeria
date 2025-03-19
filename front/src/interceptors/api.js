import axios from 'axios';
import { useAuthStore } from '@/stores/auth';
import { useErrorStore } from '@/stores/errorStore';

const api = axios.create({
    baseURL: 'http://localhost:3000/api',
    withCredentials: true,
});

api.interceptors.response.use(
    response => {
        return response;
    },
    async error => {
        const originalRequest = error.config;
        const authStore = useAuthStore();

        // Vérification des erreurs d'authentification
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            try {
                const response = await axios.post('http://localhost:3000/api/auth/refresh', {}, { withCredentials: true });
                const newAccessToken = response.data.accessToken;

                // Mise à jour du token dans le store et le localStorage
                authStore.accessToken = newAccessToken;
                localStorage.setItem('accessToken', newAccessToken);
                api.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`;
                originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;

                return api(originalRequest);
            } catch (refreshError) {
                console.log('Erreur de rafraîchissement du token:', refreshError);
                authStore.clearAuth();
            }
        }

        const errorStore = useErrorStore();
        const message = error.response?.data?.message || 'Une erreur est survenue';
        errorStore.setError(message);
        console.log('Message d\'erreur capturé:', message);
        return Promise.reject(error);
    }
);

export default api;
