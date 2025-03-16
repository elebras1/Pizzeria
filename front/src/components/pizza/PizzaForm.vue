<template>
    <div class="form-container">
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

            <div v-if="error" class="error-message">
                {{ error }}
            </div>

            <button type="submit" class="submit-button">
                {{ isEditMode ? 'Mettre à jour' : 'Créer' }}
            </button>
        </form>
    </div>
</template>

<script>
import pizzaService from '@/services/pizzaService';
import ingredientService from '@/services/ingredientService';

export default {
    name: 'PizzaForm',
    props: {
        pizzaId: {
            type: Number,
            default: null
        }
    },
    data() {
        return {
            pizza: {
                nom: '',
                description: '',
                photo: null,
                standardIngredientsIds: [],
                optionalIngredientsIds: []
            },
            ingredients: [],
            error: null
        };
    },
    computed: {
        isEditMode() {
            return this.pizzaId !== null;
        },
        photoPreviewUrl() {
            return this.pizza.photo ? URL.createObjectURL(this.pizza.photo) : '';
        }
    },
    methods: {
        handleSubmit() {
            this.error = null;
            const formData = new FormData();
            formData.append('nom', this.pizza.nom);
            formData.append('description', this.pizza.description);
            if (this.pizza.photo && this.pizza.photo instanceof File) {
                formData.append('photo', this.pizza.photo);
            }

            this.pizza.standardIngredientsIds.forEach(id => formData.append('standardIngredientsIds', id));
            this.pizza.optionalIngredientsIds.forEach(id => formData.append('optionalIngredientsIds', id));

            if (this.isEditMode) {
                pizzaService.updatePizza(this.pizzaId, formData)
                    .then(() => {
                        this.$router.push({ name: 'PizzaList' });
                    })
                    .catch(error => {
                        console.error('Erreur lors de la mise à jour', error);
                        this.error = 'Erreur lors de la mise à jour de la pizza.';
                    });
            } else {
                pizzaService.createPizza(formData)
                    .then(() => {
                        this.$router.push({ name: 'PizzaList' });
                    })
                    .catch(error => {
                        console.error('Erreur lors de la création', error);
                        this.error = 'Erreur lors de la création de la pizza.';
                    });
            }
        },
        onFileChange(e) {
            const file = e.target.files[0];
            if (file) {
                this.pizza.photo = file;
            }
        },
        fetchPizza() {
            if (this.isEditMode) {
                pizzaService.getPizza(this.pizzaId)
                    .then(response => {
                        this.pizza = response.data;
                        this.pizza.standardIngredientsIds = this.pizza.standardIngredients.map(ing => ing.id);
                        this.pizza.optionalIngredientsIds = this.pizza.optionalIngredients.map(ing => ing.id);
                    })
                    .catch(error => {
                        console.error('Erreur lors de la récupération de la pizza', error);
                        this.error = 'Erreur lors de la récupération de la pizza.';
                    });
            }
        },
        fetchIngredients() {
            ingredientService.getIngredients()
                .then(response => {
                    this.ingredients = response.data;
                })
                .catch(error => {
                    console.error('Erreur lors de la récupération des ingrédients', error);
                    this.error = 'Erreur lors de la récupération des ingrédients.';
                });
        }
    },
    mounted() {
        this.fetchIngredients();
        this.fetchPizza();
    }
};
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

.error-message {
    color: red;
    text-align: center;
    font-weight: bold;
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