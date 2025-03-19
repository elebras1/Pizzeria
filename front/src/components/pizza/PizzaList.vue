<template>
    <div v-if="isAdmin">
        <h1>Liste des pizzas</h1>
        <div>
            <router-link :to="{ name: 'PizzaCreate' }" class="action-button add">
                Ajouter
            </router-link>
        </div>
        <table class="custom-table" v-if="pizzas.length">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Description</th>
                    <th>Photo</th>
                    <th>Ingrédients Standards</th>
                    <th>Ingrédients Optionnels</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="pizza in pizzas" :key="pizza.id">
                    <td>{{ pizza.id }}</td>
                    <td>{{ pizza.nom }}</td>
                    <td>{{ pizza.description }}</td>
                    <td>
                        <!-- Affichage de l'image récupérée -->
                        <img :src="pizza.photoUrl" :alt="`Photo de ${pizza.nom}`" class="pizza-photo" />
                    </td>
                    <td>
                        <span v-for="(ingredient, index) in pizza.standardIngredients" :key="ingredient.id">
                            {{ ingredient.nom }}<span v-if="index < pizza.standardIngredients.length - 1">, </span>
                        </span>
                    </td>
                    <td>
                        <span v-for="(ingredient, index) in pizza.optionalIngredients" :key="ingredient.id">
                            {{ ingredient.nom }}<span v-if="index < pizza.optionalIngredients.length - 1">, </span>
                        </span>
                    </td>
                    <td>
                        <router-link :to="{ name: 'PizzaEdit', params: { id: pizza.id } }" class="action-button edit">
                            Modifier
                        </router-link>
                        <button @click="deletePizza(pizza.id)" class="action-button delete">
                            Supprimer
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
        <p v-else>Aucune pizza disponible.</p>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import pizzaService from '@/services/pizzaService';
import imageService from '@/services/imageService';

const router = useRouter();
const authStore = useAuthStore();
const isAdmin = ref(false);
const pizzas = ref([]);

const fetchPizzas = () => {
    pizzaService.getPizzas()
        .then(response => {
            pizzas.value = response.data;
            pizzas.value.forEach(pizza => {
                imageService.getImage(pizza.photo)
                    .then(imageResponse => {
                        pizza.photoUrl = URL.createObjectURL(imageResponse.data);
                        console.log('Image récupérée', pizza.photoUrl);
                    })
                    .catch(error => {
                        console.error('Erreur lors de la récupération de l\'image', error);
                    });
            });
        })
        .catch(error => {
            console.error('Erreur lors de la récupération des pizzas', error);
        });
};

const deletePizza = (id) => {
    if (confirm("Voulez-vous vraiment supprimer cette pizza ?")) {
        pizzaService.deletePizza(id)
            .then(() => {
                fetchPizzas();
            })
            .catch(error => {
                console.error('Erreur lors de la suppression', error);
            });
    }
};

onMounted(async () => {
    try {
        const response = await authStore.verifyAdmin();
        const compteDto = response.data;
        if (compteDto && compteDto.isAdmin) {
            isAdmin.value = true;
            fetchPizzas();
        } else {
            router.push({ name: 'Home' });
        }
    } catch (error) {
        console.error("Erreur lors de la vérification admin :", error);
        router.push({ name: 'Home' });
    }
});
</script>

<style scoped>
.custom-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 1rem;
    border-radius: 8px;
    overflow: hidden;
}

.custom-table th,
.custom-table td {
    border: 1px solid #ddd;
    padding: 10px;
    text-align: left;
    border-radius: 4px;
}

.custom-table th {
    background-color: #4CAF50;
    color: white;
}

.custom-table tr:nth-child(even) {
    background-color: #f9f9f9;
}

.custom-table tr:hover {
    background-color: #f1f1f1;
}

.pizza-photo {
    max-width: 100px;
    height: auto;
    border-radius: 5px;
}

.action-button {
    padding: 8px 15px;
    border-radius: 5px;
    border: none;
    cursor: pointer;
    margin-right: 5px;
    font-size: 0.9rem;
    text-decoration: none;
    display: inline-block;
    text-align: center;
    transition: all 0.3s ease;
}

.action-button.edit {
    background-color: #007BFF;
    color: white;
}

.action-button.delete {
    background-color: #DC3545;
    color: white;
}

.action-button.add {
    background-color: #28a745;
    color: white;
}

.error-message {
    color: red;
    font-weight: bold;
    margin-top: 10px;
}
</style>