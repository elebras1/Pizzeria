import { useAuthStore } from '@/stores/auth';

class ChatService {
    constructor() {
        this.socket = null;
        this.callbacks = new Map();
    }

    connect() {
        const authStore = useAuthStore();
        const token = authStore.accessToken;
        this.socket = new WebSocket(`ws://localhost:8081/api/chat?token=${token}`);

        this.socket.onmessage = (event) => {
            const message = JSON.parse(event.data);
            const callback = this.callbacks.get(message.receveurId);
            if (callback) {
                callback(message);
            }
        };

        this.socket.onclose = () => {
            console.log("WebSocket disconnected. Reconnecting...");
            setTimeout(() => this.connect(), 1000);
        };
    }

    sendMessage(message) {
        this.socket.send(JSON.stringify(message));
    }

    onMessage(receveurId, callback) {
        this.callbacks.set(receveurId, callback);
    }
}

const chatService = new ChatService();
export default chatService;
