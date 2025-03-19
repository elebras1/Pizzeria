<template>
    <div class="form-container" v-if="isAdmin">
        <h1>{{ isEditMode ? 'Modifier' : 'Créer' }} un ingrédient</h1>

        <form @submit.prevent="handleSubmit">
            <div class="form-group">
                <label>Nom</label>
                <input v-model="ingredient.nom" type="text" required>
            </div>

            <div class="form-group">
                <label>Description</label>
                <textarea v-model="ingredient.description" required></textarea>
            </div>

            <div class="form-group">
                <label>Prix</label>
                <input v-model="ingredient.prix" type="number" step="any" required>
            </div>

            <button type="submit" class="submit-button">
                {{ isEditMode ? 'Mettre à jour' : 'Créer' }}
            </button>
        </form>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import ingredientService from '@/services/ingredientService';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const isAdmin = ref(false);
const ingredient = ref({
    nom: '',
    description: '',
    photo: '',
    prix: 0
});

const ingredientId = computed(() => {
    return route.params.id ? parseInt(route.params.id) : null;
});

const isEditMode = computed(() => {
    return ingredientId.value !== null;
});

const handleSubmit = () => {
    if (isEditMode.value) {
        ingredientService.updateIngredient(ingredientId.value, ingredient.value)
            .then(() => {
                router.push({ name: 'IngredientList' });
            })
            .catch(error => {
                console.error('Erreur lors de la mise à jour', error);
            });
    } else {
        ingredientService.createIngredient(ingredient.value)
            .then(() => {
                router.push({ name: 'IngredientList' });
            })
            .catch(error => {
                console.error('Erreur lors de la création', error);
            });
    }
};

const fetchIngredient = () => {
    if (isEditMode.value) {
        ingredientService.getIngredient(ingredientId.value)
            .then(response => {
                ingredient.value = response.data;
            })
            .catch(error => {
                console.error('Erreur lors de la récupération de l\'ingrédient', error);
            });
    }
};

onMounted(async () => {
    try {
        const response = await authStore.verifyAdmin();
        const compteDto = response.data;
        if (compteDto && compteDto.isAdmin) {
            isAdmin.value = true;
            fetchIngredient();
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
.form-container {
    max-width: 500px;
    margin: auto;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h1 {
    text-align: center;
    color: #333;
}

.form-group {
    display: flex;
    flex-direction: column;
    margin-bottom: 15px;
}

label {
    font-weight: bold;
    margin-bottom: 5px;
    color: #555;
}

input,
textarea,
select {
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
    font-size: 1rem;
}

textarea {
    resize: vertical;
    min-height: 80px;
}

input:focus,
textarea:focus,
select:focus {
    border-color: #007bff;
    outline: none;
    box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
}

.image-preview img {
    max-width: 100%;
    border-radius: 5px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    margin-top: 10px;
}

.submit-button {
    width: 100%;
    padding: 10px;
    background-color: #007bff;
    border: none;
    border-radius: 5px;
    color: white;
    font-size: 1.1rem;
    cursor: pointer;
    transition: background 0.3s;
}

.submit-button:hover {
    background-color: #0056b3;
}
</style>