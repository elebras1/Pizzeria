import axios from 'axios';

const API_URL = 'http://localhost:8081/api/commandes';

export default {
    getCommandes() {
        return axios.get(API_URL);
    },
    getCommande(id) {
        return axios.get(`${API_URL}/${id}`);
    },
    getCommentaires(id) {
        return axios.get(`${API_URL}/${id}/commentaires`);
    },
    createCommande(commande) {
        return axios.post(API_URL, commande);
    },
    updateCommande(id, commande) {
        return axios.put(`${API_URL}/${id}`, commande);
    },
    deleteCommande(id) {
        return axios.delete(`${API_URL}/${id}`);
    }
};
