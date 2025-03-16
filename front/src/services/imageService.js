import axios from 'axios';

const API_URL = 'http://localhost:8081/api/images';

export default {
    getImage(imageUrl) {
        return axios.get(`${API_URL}/${imageUrl}`, { responseType: 'blob' });
    }
};
