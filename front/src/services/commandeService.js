import api from "@/interceptors/api";

const API_URL = '/commandes';

export default {
    getCommandes() {
        return api.get(API_URL);
    },
    getCommande(id) {
        return api.get(`${API_URL}/${id}`);
    },
    getCommentaires(id) {
        return api.get(`${API_URL}/${id}/commentaires`);
    },
    addCommentaireToCommande(id, commentaire) {
        return api.post(`${API_URL}/${id}/commentaires`, commentaire);
    },
    createCommande(commande) {
        return api.post(API_URL, commande);
    },
    updateCommande(id, commande) {
        return api.put(`${API_URL}/${id}`, commande);
    },
    deleteCommande(id) {
        return api.delete(`${API_URL}/${id}`);
    },
    finishCommande(id) {
        return api.put(`${API_URL}/${id}/finish`);
    }
};
