import axios from 'axios';

const API_URL = '/commentaires';

export default {
    getCommentaires() {
        return api.get(API_URL);
    },
    getCommentaire(id) {
        return api.get(`${API_URL}/${id}`);
    },
    updateCommentaire(id, commentaire) {
        return api.put(`${API_URL}/${id}`, commentaire);
    },
    deleteCommentaire(id) {
        return api.delete(`${API_URL}/${id}`);
    }
};
