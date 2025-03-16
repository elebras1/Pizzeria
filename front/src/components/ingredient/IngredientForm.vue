<template>
    <div class="form-container">
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
import ingredientService from '@/services/ingredientService';

export default {
    name: 'IngredientForm',
    props: {
        ingredientId: {
            type: Number,
            default: null
        }
    },
    data() {
        return {
            ingredient: {
                nom: '',
                description: '',
                photo: '',
                prix: 0
            },
            ingredients: [],
            error: null
        };
    },
    computed: {
        isEditMode() {
            return this.ingredientId !== null;
        }
    },
    methods: {
        handleSubmit() {
            this.error = null;
            if (this.isEditMode) {
                ingredientService.updateIngredient(this.ingredientId, this.ingredient)
                    .then(() => {
                        this.$router.push({ name: 'IngredientList' });
                    })
                    .catch(error => {
                        console.error('Erreur lors de la mise à jour', error);
                        this.error = 'Erreur lors de la mise à jour de l\'ingrédient.';
                    });
            } else {
                ingredientService.createIngredient(this.ingredient)
                    .then(() => {
                        this.$router.push({ name: 'IngredientList' });
                    })
                    .catch(error => {
                        console.error('Erreur lors de la création', error);
                        this.error = 'Erreur lors de la création de l\'ingrédient.';
                    });
            }
        },
        fetchIngredient() {
            if (this.isEditMode) {
                ingredientService.getIngredient(this.ingredientId)
                    .then(response => {
                        this.ingredient = response.data;
                    })
                    .catch(error => {
                        console.error('Erreur lors de la récupération de la ingredient', error);
                        this.error = 'Erreur lors de la récupération de la ingredient.';
                    });
            }
        },
        onFileChange(e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (event) => {
                    // Stocke l'image au format Base64 dans ingredient.photo
                    this.ingredient.photo = event.target.result;
                };
                reader.readAsDataURL(file);
            }
        }
    },
    mounted() {
        this.fetchIngredient();
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