const express = require('express');
const app = express();
const dotenv = require('dotenv');

dotenv.config();

const authRoutes = require('./routes/auth');

app.use(express.json());

app.use('/api/auth', authRoutes);

const port = process.env.PORT || 5000;
app.listen(port, () => {
  console.log(`Serveur API d'authentification démarré sur http://localhost:${port}`);
});