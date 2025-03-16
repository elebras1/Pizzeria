import { createRouter, createWebHistory } from 'vue-router';
import Home from "../components/Home.vue";
import PizzaSelection from "@/components/PizzaSelection.vue";
import Login from "@/components/authentification/Login.vue";
import Register from "@/components/authentification/Register.vue";
import PizzaList from '@/components/pizza/PizzaList.vue';
import PizzaForm from '@/components/pizza/PizzaForm.vue';
import IngredientList from '@/components/ingredient/IngredientList.vue';
import IngredientForm from '@/components/ingredient/IngredientForm.vue';
import CommandeList from '@/components/commande/CommandeList.vue';

const routes = [
    { path: '/login', name: 'Login', component: Login },
    { path: '/register', name: 'Register', component: Register },
    { path: '/', name: 'Home', component: Home },
    { path: '/pizza-selection/:id', name: 'PizzaSelection', component: PizzaSelection },
    { path: '/administration/pizzas', name: 'PizzaList', component: PizzaList },
    { path: '/administration/pizzas/new', name: 'PizzaCreate', component: PizzaForm },
    { path: '/administration/pizzas/edit/:id', name: 'PizzaEdit', component: PizzaForm, props: route => ({ pizzaId: Number(route.params.id) }) },
    { path: '/administration/ingredients', name: 'IngredientList', component: IngredientList },
    { path: '/administration/ingredients/new', name: 'IngredientCreate', component: IngredientForm },
    { path: '/administration/ingredients/edit/:id', name: 'IngredientEdit', component: IngredientForm, props: route => ({ ingredientId: Number(route.params.id) }) },
    { path: '/administration/commandes', name: 'CommandeList', component: CommandeList }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
