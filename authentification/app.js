const express = require('express');
const httpProxy = require('http-proxy');
const jwt = require('jsonwebtoken');
const proxy = httpProxy.createProxyServer();
const app = express();
require('dotenv').config();

const publicRoutes = [
    '/api/pizzas',
    '/api/auth/login',
    '/api/auth/register'    
];

function authMiddleware(req, res, next) {
    if (publicRoutes.includes(req.path)) {
        return next();
    }

    const token = req.headers.authorization?.split(' ')[1];
    if (!token) {
        return res.status(401).json({ message: 'Accès non autorisé, jeton manquant' });
    }

    try {
        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        req.user = decoded;
        next();
    } catch (error) {
        return res.status(401).json({ message: 'Jeton invalide ou expiré' });
    }
}


app.use(authMiddleware);

app.all('/*', (req, res) => {
    proxy.web(req, res, { target: 'http://localhost:8080' });
});

app.listen(3000, () => {
    console.log('Serveur Node.js en écoute sur le port 3000');
});
