import './assets/styles.css';

import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import {useAuthStore} from "@/stores/auth.js";
import {usePanierStore} from '@/stores/panier';

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);

const authStore = useAuthStore();
authStore.initialize();

usePanierStore().loadPanier();

app.use(router);
app.mount('#app');
