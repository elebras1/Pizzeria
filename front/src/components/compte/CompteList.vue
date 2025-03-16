<template>
    <div>
        <h1>Liste des comptes</h1>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        <table class="custom-table" v-if="comptes.length">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Pseudo</th>
                    <th>Prénom</th>
                    <th>nom</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="compte in comptes" :key="compte.id">
                    <td>{{ compte.id }}</td>
                    <td>{{ compte.pseudo }}</td>
                    <td>{{ compte.prenom }}</td>
                    <td>{{ compte.nom }}</td>
                    <td>
                        <router-link :to="{ name: 'CommandeListByCompte', params: { idCompte: compte.id } }"
                            class="action-button show">
                            Voir Commandes
                        </router-link>
                        <button @click="deleteCompte(compte.id)" class="action-button delete">
                            Supprimer
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
        <p v-else>Aucun compte disponible.</p>
    </div>
</template>

<script>
import compteService from '@/services/compteService';

export default {
    name: 'CompteList',
    data() {
        return {
            comptes: [],
            errorMessage: ''
        };
    },
    methods: {
        fetchComptes() {
            compteService.getComptes()
                .then(response => {
                    this.comptes = response.data;
                })
                .catch(error => console.error('Erreur lors de la récupération des comptes', error));
        },
        deleteCompte(id) {
            if (confirm("Voulez-vous vraiment supprimer ce compte ?")) {
                compteService.deleteCompte(id)
                    .then(() => {
                        this.fetchComptes();
                        this.errorMessage = '';
                    })
                    .catch(error => {
                        console.error('Erreur lors de la suppression', error);
                        this.errorMessage = 'Erreur : Impossible de supprimer ce compte.';
                    });
            }
        }

    },
    mounted() {
        this.fetchComptes();
    }
};
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
    font-size: 0.9rem;
    text-decoration: none;
    display: inline-block;
    text-align: center;
    transition: all 0.3s ease;
    margin: 2px;
}

.action-button.edit {
    background-color: #007BFF;
    color: white;
}

.action-button.delete {
    background-color: #DC3545;
    color: white;
}

.action-button.show {
    background-color: #28a745;
    color: white;
}

.error-message {
    color: red;
    font-weight: bold;
    margin-top: 10px;
}
</style>
