import './assets/styles.css';

import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import {useAuthStore} from "@/stores/auth.js";
import {useErrorStore} from "@/stores/errorStore.js";

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);

useAuthStore().initialize();

useErrorStore().clearError();

app.use(router);
app.mount('#app');
