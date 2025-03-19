import './assets/styles.css';

import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import {useAuthStore} from "@/stores/auth.js";
import {usePanierStore} from '@/stores/panier';
import {useErrorStore} from "@/stores/errorStore.js";

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);

const authStore = useAuthStore();
authStore.initialize();

usePanierStore().loadPanier();
useErrorStore().clearError();

app.use(router);
app.mount('#app');
