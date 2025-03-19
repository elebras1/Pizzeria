<template>
    <div v-if="isAdmin">
        <h1>Liste des ingrédients</h1>
        <div>
            <router-link :to="{ name: 'IngredientCreate' }" class="action-button add">
                Ajouter
            </router-link>
        </div>
        <table class="custom-table" v-if="ingredients.length">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Description</th>
                    <th>Prix</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="ingredient in ingredients" :key="ingredient.id">
                    <td>{{ ingredient.id }}</td>
                    <td>{{ ingredient.nom }}</td>
                    <td>{{ ingredient.description }}</td>
                    <td>{{ ingredient.prix }}</td>
                    <td>
                        <router-link :to="{ name: 'IngredientEdit', params: { id: ingredient.id } }"
                            class="action-button edit">
                            Modifier
                        </router-link>
                        <button @click="deleteIngredient(ingredient.id)" class="action-button delete">
                            Supprimer
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
        <p v-else>Aucun ingrédient disponible.</p>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import ingredientService from '@/services/ingredientService';

const router = useRouter();
const authStore = useAuthStore();
const isAdmin = ref(false);
const ingredients = ref([]);

const fetchIngredients = () => {
    ingredientService.getIngredients()
        .then(response => {
            ingredients.value = response.data;
        })
        .catch(error => console.error('Erreur lors de la récupération des ingrédients', error));
};

const deleteIngredient = (id) => {
    if (confirm("Voulez-vous vraiment supprimer cet ingrédient ?")) {
        ingredientService.deleteIngredient(id)
            .then(() => {
                fetchIngredients();
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
            fetchIngredients();
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