import axios from 'axios';

const API_URL = 'http://localhost:8081/api/comptes';

export default {
    getComptes() {
        return axios.get(API_URL);
    },
    getCompte(id) {
        return axios.get(`${API_URL}/${id}`);
    },
    createCompte(compte) {
        return axios.post(API_URL, compte);
    },
    updateCompte(id, compte) {
        return axios.put(`${API_URL}/${id}`, compte);
    },
    deleteCompte(id) {
        return axios.delete(`${API_URL}/${id}`);
    }
};
