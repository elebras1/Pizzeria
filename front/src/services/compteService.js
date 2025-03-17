import api from '@/interceptors/api';

const API_URL = '/comptes';

export default {
    getComptes() {
        return api.get(API_URL);
    },
    getCompte(id) {
        return api.get(`${API_URL}/${id}`);
    },
    getCommandesById(id) {
        return api.get(`${API_URL}/${id}/commandes`);
    },
    getCommandes() {
        return api.get(`${API_URL}/commandes`);
    },
    createCompte(compte) {
        return api.post(API_URL, compte);
    },
    updateCompte(id, compte) {
        return api.put(`${API_URL}/${id}`, compte);
    },
    deleteCompte(id) {
        return api.delete(`${API_URL}/${id}`);
    }
};
