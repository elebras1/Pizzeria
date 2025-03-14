import axios from 'axios';
import { useAuthStore } from '@/stores/auth';

const api = axios.create({
    baseURL: 'http://localhost:3000/api',
    withCredentials: true,
});

api.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error.config;
        const authStore = useAuthStore();
        if (error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            try {
                const response = await axios.post('/api/auth/refresh', {}, { withCredentials: true });
                const newAccessToken = response.data.accessToken;
                api.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`;
                originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
                return api(originalRequest);
            } catch (refreshError) {
                console.log('Erreur de rafraîchissement du token:', refreshError);
                authStore.isLoggedIn = false;
                authStore.accessToken = null;
                return Promise.reject(refreshError);
            }
        }
        authStore.isLoggedIn = false;
        authStore.accessToken = null;
        return Promise.reject(error);
    }
);

export default api;
