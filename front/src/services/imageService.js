import api from '@/interceptors/api';

const API_URL = '/images';

export default {
    getImage(imageUrl) {
        return api.get(`${API_URL}/${imageUrl}`, { responseType: 'blob' });
    }
};
