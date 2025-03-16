import { createRouter, createWebHistory } from 'vue-router';
import Home from "../components/Home.vue";
import Login from "@/components/authentification/Login.vue";
import Register from "@/components/authentification/Register.vue";
import Panier from "@/components/Panier.vue";
import Compte from "@/components/Compte.vue";

const routes = [
    { path: '/login', name: 'Login', component: Login },
    { path: '/register', name: 'Register', component: Register },
    { path: '/', name: 'Home', component: Home },
    { path: '/panier', name : 'panier', component: Panier },
    {path: '/account', name: 'Compte', component: Compte },
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
