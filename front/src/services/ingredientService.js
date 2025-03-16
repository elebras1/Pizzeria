import axios from 'axios';

const API_URL = 'http://localhost:8081/api/ingredients';

export default {
    getIngredients() {
        return axios.get(API_URL);
    },
    getIngredient(id) {
        return axios.get(`${API_URL}/${id}`);
    },
    createIngredient(ingredient) {
        return axios.post(API_URL, ingredient);
    },
    updateIngredient(id, ingredient) {
        return axios.put(`${API_URL}/${id}`, ingredient);
    },
    deleteIngredient(id) {
        return axios.delete(`${API_URL}/${id}`);
    }
};
