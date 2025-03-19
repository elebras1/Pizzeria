<script>
import { Bar, Doughnut, Line } from 'vue-chartjs';
import {
  Chart as ChartJS,
  Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, ArcElement, PointElement, LineElement
} from 'chart.js';
import api from "@/interceptors/api.js";
import commandeService  from "@/services/commandeService.js";

ChartJS.register(
    Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale,
    ArcElement, PointElement, LineElement
);

export default {
  name: "Statistique",
  components: {
    Bar, Doughnut, Line
  },
  data() {
    return {
      Commandes: [],
      chartData: {
        topPizzas: {
          labels: [],
          datasets: [{
            label: "Top des Pizzas les plus vendues",
            data: [],
            backgroundColor: ["#3498db", "#e74c3c", "#2ecc71", "#f39c12", "#9b59b6"]
          }]
        },
        monthlySales: {
          labels: [],
          datasets: [{
            label: "Nombre de pizzas vendues par mois",
            data: [],
            backgroundColor: "#2ecc71"
          }]
        },
        pizzaDistribution: {
          labels: [],
          datasets: [{
            label: "Répartition des ventes",
            data: [],
            backgroundColor: ["#3498db", "#e74c3c", "#2ecc71", "#f39c12", "#9b59b6", "#95a5a6"]
          }]
        }
      },
      chartOptions: {
        responsive: true,
        plugins: {
          legend: { position: "top" },
          title: { display: true, text: "Statistiques des Pizzas" }
        }
      },
      pizzasStats: [],
      topPizzas: [],
      monthlySales: [],
    };
  },
  methods: {
    getCommandes() {
      commandeService.getCommandes().then((response) => {
        this.Commandes = response.data || [];
        if (this.Commandes.length > 0) {
          this.processData();
        } else {
          console.warn("Aucune commande trouvée.");
        }
      }).catch((error) => {
        console.error("Erreur lors de la récupération des commandes :", error);
      });
    },

    processData() {
      console.log("Commandes à traiter :", this.Commandes);
      if (!this.Commandes || this.Commandes.length === 0) {
        console.warn("Aucune commande à traiter.");
        return;
      }

      const pizzaCounts = new Map();
      const pizzaRevenue = new Map();
      const monthSales = new Map();
      const mois = [
        "janvier", "février", "mars", "avril", "mai", "juin",
        "juillet", "août", "septembre", "octobre", "novembre", "décembre"
      ];
      mois.forEach((mois) => monthSales.set(mois, 0));


      const currentDate = new Date();
      const currentMonth = currentDate.getMonth();
      const currentYear = currentDate.getFullYear();

      this.Commandes.forEach((commande) => {
        commande.panier.forEach((item) => {
          const pizzaName = item.pizza.nom;

          // Nombre de ventes par pizza
          pizzaCounts.set(pizzaName, (pizzaCounts.get(pizzaName) || 0) + 1);

          // Recette générée par pizza
          const totalPrix = item.ingredients.reduce((sum, ingredient) => sum + ingredient.prix, 0);
          pizzaRevenue.set(pizzaName, (pizzaRevenue.get(pizzaName) || 0) + totalPrix);

          // Ventes par mois
          const month = new Date(commande.date).toLocaleString('fr-FR', { month: 'long' });
          monthSales.set(month, (monthSales.get(month) || 0) + 1);
        });
      });

      this.pizzasStats = Array.from(pizzaCounts.entries()).map(([name, count]) => ({ name, count }));
      this.topPizzas = this.pizzasStats.sort((a, b) => b.count - a.count).slice(0, 5);
      this.monthlySales = Array.from(monthSales.entries());

      this.buildCharts();
    },

    buildCharts() {
      // Top 5 des pizzas les plus vendues
      this.chartData.topPizzas = {
        labels: this.topPizzas.map(p => p.name),
        datasets: [{
          label: "Top 5 des Pizzas les plus vendues",
          data: this.topPizzas.map(p => p.count),
          backgroundColor: ["#3498db", "#e74c3c", "#2ecc71", "#f39c12", "#9b59b6"]
        }]
      };

      // Ventes mensuelles
      this.chartData.monthlySales = {
        labels: this.monthlySales.map(m => m[0]),
        datasets: [{
          label: "Nombre de pizzas vendues par mois",
          data: this.monthlySales.map(m => m[1]),
          backgroundColor: "#2ecc71"
        }]
      };

      // Répartition des ventes par pizza
      this.chartData.pizzaDistribution = {
        labels: this.pizzasStats.map(p => p.name),
        datasets: [{
          label: "Répartition des ventes",
          data: this.pizzasStats.map(p => p.count),
          backgroundColor: ["#3498db", "#e74c3c", "#2ecc71", "#f39c12", "#9b59b6", "#95a5a6"]
        }]
      };

      // Options de graphique
      this.chartOptions = {
        responsive: true,
        plugins: {
          legend: { position: "top" },
          title: { display: true, text: "Statistiques des Pizzas" }
        }
      };
    }
  },

  mounted() {
    this.getCommandes();
  }
};
</script>

<template>
  <div>
    <h2>Statistiques des Pizzas</h2>
    <div>
      <h3>Top 5 des pizzas les plus vendues</h3>
      <Bar :data="chartData.topPizzas" :options="chartOptions" />
    </div>

    <div>
      <h3>Ventes mensuelles</h3>
      <Line :data="chartData.monthlySales" :options="chartOptions" />
    </div>

    <div>
      <h3>Répartition des ventes par pizza</h3>
      <Doughnut :data="chartData.pizzaDistribution" :options="chartOptions" />
    </div>
  </div>
</template>

<style scoped>
h2, h3 {
  text-align: center;
}
.chart-container {
  margin: 20px auto;
  max-width: 800px;
}
</style>
