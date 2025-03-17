import axios from 'axios';

const API_URL = 'http://localhost:8081/api/commentaires';

export default {
    getCommentaires() {
        return axios.get(API_URL);
    },
    getCommentaire(id) {
        return axios.get(`${API_URL}/${id}`);
    },
    updateCommentaire(id, commentaire) {
        return axios.put(`${API_URL}/${id}`, commentaire);
    },
    deleteCommentaire(id) {
        return axios.delete(`${API_URL}/${id}`);
    }
};
