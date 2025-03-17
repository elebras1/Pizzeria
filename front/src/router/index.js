import { createRouter, createWebHistory } from 'vue-router';
import Home from "../components/Home.vue";
import PizzaSelection from "@/components/PizzaSelection.vue";
import Login from "@/components/authentification/Login.vue";
import Register from "@/components/authentification/Register.vue";
import Commande from "@/components/Commande.vue";
import Compte from "@/components/Compte.vue";
import Panier from "@/components/Panier.vue";
import CompteList from "@/components/compte/CompteList.vue";
import CommandeList from "@/components/commande/CommandeList.vue";
import IngredientForm from "@/components/ingredient/IngredientForm.vue";
import IngredientList from "@/components/ingredient/IngredientList.vue";
import PizzaForm from "@/components/pizza/PizzaForm.vue";
import PizzaList from "@/components/pizza/PizzaList.vue";

const routes = [
    { path: '/login', name: 'Login', component: Login },
    { path: '/register', name: 'Register', component: Register },
    { path: '/', name: 'Home', component: Home },
    { path: '/panier', name : 'panier', component: Panier },
    {path: '/account', name: 'Compte', component: Compte },
    {path: '/account/commandes', name: 'Commande', component: Commande },
    { path: '/administration/pizzas', name: 'PizzaList', component: PizzaList },
    { path: '/administration/pizzas/new', name: 'PizzaCreate', component: PizzaForm },
    { path: '/administration/pizzas/edit/:id', name: 'PizzaEdit', component: PizzaForm, props: route => ({ pizzaId: Number(route.params.id) }) },
    { path: '/administration/ingredients', name: 'IngredientList', component: IngredientList },
    { path: '/administration/ingredients/new', name: 'IngredientCreate', component: IngredientForm },
    { path: '/administration/ingredients/edit/:id', name: 'IngredientEdit', component: IngredientForm, props: route => ({ ingredientId: Number(route.params.id) }) },
    { path: '/administration/commandes', name: 'CommandeListAll', component: CommandeList },
    { path: '/administration/commandes/:idCompte', name: 'CommandeListByCompte', component: CommandeList, props: route => ({ idCompte: Number(route.params.idCompte) }) },
    { path: '/administration/comptes', name: 'CompteList', component: CompteList }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
