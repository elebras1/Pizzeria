import { createRouter, createWebHistory } from 'vue-router';
import Home from "../components/Home.vue";
import PizzaSelection from "@/components/PizzaSelection.vue";
import Login from "@/components/authentification/Login.vue";
import Register from "@/components/authentification/Register.vue";
import PizzaList from '@/components/pizza/PizzaList.vue';
import PizzaForm from '@/components/pizza/PizzaForm.vue';

const routes = [
    { path: '/login', name: 'Login', component: Login },
    { path: '/register', name: 'Register', component: Register },
    { path: '/', name: 'Home', component: Home },
    { path: '/pizza-selection/:id', name: 'PizzaSelection', component: PizzaSelection },
    { path: '/administration/pizzas', name: 'PizzaList', component: PizzaList },
    { path: '/administration/pizzas/new', name: 'PizzaCreate', component: PizzaForm },
    { path: '/administration/pizzas/edit/:id', name: 'PizzaEdit', component: PizzaForm, props: route => ({ pizzaId: Number(route.params.id) }) }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
