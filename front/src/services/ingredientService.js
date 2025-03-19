import api from '@/interceptors/api';
import axios from 'axios';

const API_URL = '/ingredients';

export default {
    getIngredients() {
        return api.get(API_URL);
    },
    getIngredient(id) {
        return api.get(`${API_URL}/${id}`);
    },
    createIngredient(ingredient) {
        return api.post(API_URL, ingredient);
    },
    updateIngredient(id, ingredient) {
        return api.put(`${API_URL}/${id}`, ingredient);
    },
    deleteIngredient(id) {
        return api.delete(`${API_URL}/${id}`);
    }
};
