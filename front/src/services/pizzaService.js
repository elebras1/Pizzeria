import axios from 'axios';

const API_URL = 'http://localhost:8081/api/pizzas';

export default {
    getPizzas() {
        return axios.get(API_URL);
    },
    getPizza(id) {
        return axios.get(`${API_URL}/${id}`);
    },
    createPizza(pizza) {
        return axios.post(API_URL, pizza);
    },
    updatePizza(id, pizza) {
        return axios.put(`${API_URL}/${id}`, pizza);
    },
    deletePizza(id) {
        return axios.delete(`${API_URL}/${id}`);
    }
};
