import api from '@/interceptors/api';

const API_URL = '/pizzas';

export default {
    getPizzas() {
        return api.get(API_URL);
    },
    getPizza(id) {
        return api.get(`${API_URL}/${id}`);
    },
    createPizza(pizza) {
        return api.post(API_URL, pizza);
    },
    updatePizza(id, pizza) {
        return api.put(`${API_URL}/${id}`, pizza);
    },
    deletePizza(id) {
        return api.delete(`${API_URL}/${id}`);
    }
};
