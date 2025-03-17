<template>
    <div class="form-container">
        <h1>{{ isEditMode ? 'Modifier' : 'Créer' }} un commentaire</h1>

        <form @submit.prevent="handleSubmit">
            <div class="form-group">
                <label>Texte</label>
                <input v-model="commentaire.texte" type="text" required>
            </div>

            <div class="form-group">
                <label>Photo</label>
                <input type="file" @change="onFileChange" :required="!isEditMode">
                <div v-if="commentaire.photo && !isEditMode" class="image-preview">
                    <img :src="photoPreviewUrl" alt="Prévisualisation de la commentaire">
                </div>
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
import commentaireService from '@/services/commentaireService';
import commandeService from '@/services/commandeService';

export default {
    name: 'CommentaireForm',
    props: {
        commentaireId: {
            type: Number,
            default: null
        },
        commandeId: {
            type: Number,
            required: true
        }
    },
    data() {
        return {
            commentaire: {
                texte: '',
                photo: null,
            },
            error: null
        };
    },
    computed: {
        isEditMode() {
            return this.commentaireId !== null;
        },
        photoPreviewUrl() {
            return this.commentaire.photo ? URL.createObjectURL(this.commentaire.photo) : '';
        }
    },
    methods: {
        handleSubmit() {
            this.error = null;
            const formData = new FormData();
            formData.append('texte', this.commentaire.texte);
            if (this.commentaire.photo && this.commentaire.photo instanceof File) {
                formData.append('photo', this.commentaire.photo);
            }

            if (this.isEditMode) {
                commentaireService.updateCommentaire(this.commentaireId, formData)
                    .then(() => {
                        this.$router.push({ name: 'Commande' });
                    })
                    .catch(error => {
                        console.error('Erreur lors de la mise à jour', error);
                        this.error = 'Erreur lors de la mise à jour du commentaire.';
                    });
            } else {
                commandeService.addCommentaireToCommande(this.commandeId, formData)
                    .then(() => {
                        this.$router.push({ name: 'Commande' });
                    })
                    .catch(error => {
                        console.error('Erreur lors de la création', error);
                        this.error = 'Erreur lors de la création du commentaire.';
                    });
            }
        },
        onFileChange(e) {
            const file = e.target.files[0];
            if (file) {
                this.commentaire.photo = file;
            }
        },
        fetchCommentaire() {
            if (this.isEditMode) {
                commentaireService.getCommentaire(this.commentaireId)
                    .then(response => {
                        this.commentaire = response.data;
                    })
                    .catch(error => {
                        console.error('Erreur lors de la récupération du commentaire', error);
                        this.error = 'Erreur lors de la récupération du commentaire.';
                    });
            }
        }
    },
    mounted() {
        this.fetchCommentaire();
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