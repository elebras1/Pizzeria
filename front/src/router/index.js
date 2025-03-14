import { createRouter, createWebHistory } from 'vue-router';
import Home from "../components/Home.vue";
import PizzaSelection from "@/components/PizzaSelection.vue";
import Login from "@/components/authentification/Login.vue";
import Register from "@/components/authentification/Register.vue";

const routes = [
    { path: '/login', name: 'Login', component: Login },
    { path: '/register', name: 'Register', component: Register },
    { path: '/', name: 'Home', component: Home },
    { path: '/pizza-selection', name: 'PizzaSelection', component: PizzaSelection },
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
