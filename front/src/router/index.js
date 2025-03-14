import { createRouter, createWebHistory } from 'vue-router';
import LoginForm from '../components/authentification/Login.vue';
import RegisterForm from '../components/authentification/Register.vue';

const routes = [
    { path: '/', name: 'Login', component: LoginForm },
    { path: '/register', name: 'Register', component: RegisterForm }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;