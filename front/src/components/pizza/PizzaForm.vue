<template>
    <div class="form-container" v-if="isAdmin">
        <h1>{{ isEditMode ? 'Modifier' : 'Créer' }} une pizza</h1>

        <form @submit.prevent="handleSubmit">
            <div class="form-group">
                <label>Nom</label>
                <input v-model="pizza.nom" type="text" required>
            </div>

            <div class="form-group">
                <label>Description</label>
                <textarea v-model="pizza.description" required></textarea>
            </div>

            <div class="form-group">
                <label>Photo</label>
                <input type="file" @change="onFileChange" :required="!isEditMode">
                <div v-if="pizza.photo && !isEditMode" class="image-preview">
                    <img :src="photoPreviewUrl" alt="Prévisualisation de la pizza">
                </div>
            </div>


            <div class="form-group">
                <label>Ingrédients standards</label>
                <select multiple v-model="pizza.standardIngredientsIds">
                    <option v-for="ingredient in ingredients" :key="ingredient.id" :value="ingredient.id">
                        {{ ingredient.nom }}
                    </option>
                </select>
            </div>

            <div class="form-group">
                <label>Ingrédients optionnels</label>
                <select multiple v-model="pizza.optionalIngredientsIds">
                    <option v-for="ingredient in ingredients" :key="ingredient.id" :value="ingredient.id">
                        {{ ingredient.nom }}
                    </option>
                </select>
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
import pizzaService from '@/services/pizzaService';
import ingredientService from '@/services/ingredientService';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const isAdmin = ref(false);
const ingredients = ref([]);
const pizza = ref({
    nom: '',
    description: '',
    photo: null,
    standardIngredientsIds: [],
    optionalIngredientsIds: []
});

// Récupérer l'id de la pizza si dans les props
const pizzaId = computed(() => {
    return route.params.id ? parseInt(route.params.id) : null;
});

// Vérifier si édition ou création
const isEditMode = computed(() => {
    return pizzaId.value !== null;
});

// Url prévisualtion de la photo
const photoPreviewUrl = computed(() => {
    return pizza.value.photo instanceof File ? URL.createObjectURL(pizza.value.photo) : '';
});

// Gestion du changement de fichier
const onFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
        pizza.value.photo = file;
    }
};

// Soumission formulaire
const handleSubmit = () => {
    const formData = new FormData();
    formData.append('nom', pizza.value.nom);
    formData.append('description', pizza.value.description);
    if (pizza.value.photo && pizza.value.photo instanceof File) {
        formData.append('photo', pizza.value.photo);
    }

    pizza.value.standardIngredientsIds.forEach(id => formData.append('standardIngredientsIds', id));
    pizza.value.optionalIngredientsIds.forEach(id => formData.append('optionalIngredientsIds', id));

    if (isEditMode.value) {
        pizzaService.updatePizza(pizzaId.value, formData)
            .then(() => {
                router.push({ name: 'PizzaList' });
            })
            .catch(error => {
                console.error('Erreur lors de la mise à jour', error);
            });
    } else {
        pizzaService.createPizza(formData)
            .then(() => {
                router.push({ name: 'PizzaList' });
            })
            .catch(error => {
                console.error('Erreur lors de la création', error);
            });
    }
};

// Récupérer les données de la pizza pour l'édition
const fetchPizza = () => {
    if (isEditMode.value) {
        pizzaService.getPizza(pizzaId.value)
            .then(response => {
                const pizzaData = response.data;
                pizza.value = {
                    ...pizzaData,
                    standardIngredientsIds: pizzaData.standardIngredients.map(ing => ing.id),
                    optionalIngredientsIds: pizzaData.optionalIngredients.map(ing => ing.id)
                };
            })
            .catch(error => {
                console.error('Erreur lors de la récupération de la pizza', error);
            });
    }
};

// Récpérer les ingrédients
const fetchIngredients = () => {
    ingredientService.getIngredients()
        .then(response => {
            ingredients.value = response.data;
        })
        .catch(error => {
            console.error('Erreur lors de la récupération des ingrédients', error);
        });
};

// Vérifier si admin + récupérer les données
onMounted(async () => {
    try {
        const response = await authStore.verifyAdmin();
        const compteDto = response.data;
        if (compteDto && compteDto.isAdmin) {
            isAdmin.value = true;
            fetchIngredients();
            fetchPizza();
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