import { createRouter, createWebHistory } from 'vue-router';
import Home from "../components/Home.vue";
import Login from "@/components/authentification/Login.vue";
import Register from "@/components/authentification/Register.vue";
import Panier from "@/components/Panier.vue";
import Compte from "@/components/Compte.vue";
import Commande from "@/components/Commande.vue";

const routes = [
    { path: '/login', name: 'Login', component: Login },
    { path: '/register', name: 'Register', component: Register },
    { path: '/', name: 'Home', component: Home },
    { path: '/panier', name : 'panier', component: Panier },
    {path: '/account', name: 'Compte', component: Compte },
    {path: '/account/commandes', name: 'Commande', component: Commande },
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
