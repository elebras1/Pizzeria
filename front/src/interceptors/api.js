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
        let message = 'Une erreur est survenue';

        // Vérification si la réponse est de type Blob (ce qui arrive pour les requêtes d'images par exemple)
        if (error.response?.data instanceof Blob) {
            try {
                const text = await error.response.data.text();
                const parsed = JSON.parse(text);
                if (parsed.message) {
                    message = parsed.message;
                } else {
                    message = text;
                }
            } catch (e) {
                message = 'Impossible de parser le message d’erreur';
            }
        } else if (error.response?.data?.message) {
            message = error.response.data.message;
        } else if (error.response?.data) {
            message = JSON.stringify(error.response.data);
        } else if (error.message) {
            message = error.message;
        }
        errorStore.setError(message);
        console.log('Message d\'erreur capturé:', message);
        return Promise.reject(error);
    }
);

export default api;
