<template>
  <div>
    <h2>Chat</h2>
    <div v-for="msg in messages" :key="msg.id">{{ msg.expediteur.pseudo }}: {{ msg.message }}</div>
    <input v-model="messageContent" placeholder="Entrez votre message" @keyup.enter="sendMessage" />
    <button @click="sendMessage">Envoyer</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      socket: null,
      messages: [],
      messageContent: "",
      expediteurId: 3,  // À remplacer par l'ID de l'utilisateur connecté
      receveurId: 2,    // À remplacer par l'ID du destinataire
    };
  },
  mounted() {
    this.connectWebSocket();
  },
  methods: {
    connectWebSocket() {
      const token = localStorage.getItem("accessToken");
      this.socket = new WebSocket(`ws://localhost:8081/api/chat?token=${token}`);

      this.socket.onopen = () => {
        console.log("Connecté au serveur WebSocket");
      };

      this.socket.onmessage = (event) => {
        const chatMessage = JSON.parse(event.data);
        console.log("Message reçu :", chatMessage);
        this.messages.push(chatMessage);
      };

      this.socket.onerror = (error) => {
        console.error("Erreur WebSocket :", error);
      };

      this.socket.onclose = () => {
        console.log("Déconnecté du serveur WebSocket");
        setTimeout(this.connectWebSocket, 3000); // Reconnexion automatique
      };
    },
    sendMessage() {
      if (this.messageContent.trim() !== "" && this.socket.readyState === WebSocket.OPEN) {
        const chatMessage = {
          expediteur: { id: this.expediteurId, pseudo: "User1" },
          receveur: { id: this.receveurId, pseudo: "User2" },
          message: this.messageContent,
        };
        console.log("Message envoyé :", JSON.stringify(chatMessage));  // Vérification avant envoi
        this.socket.send(JSON.stringify(chatMessage));
        this.messageContent = "";
      } else {
        console.warn("Socket non connectée ou message vide !");
      }
    },
  },
};
</script>

<style scoped>
input {
  margin: 5px;
  padding: 5px;
}
</style>
