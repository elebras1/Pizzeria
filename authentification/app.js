const express = require('express');
const httpProxy = require('http-proxy');
const jwt = require('jsonwebtoken');
const cookieParser = require('cookie-parser');
const proxy = httpProxy.createProxyServer();
const app = express();
require('dotenv').config();
const axios = require('axios');
const cors = require('cors');

app.use(cookieParser());
app.use(cors({
    origin: process.env.FRONT,
    credentials: true,
}));

// Routes publiques sans authentification
const publicRoutes = [
    '/api/auth/login',
    '/api/auth/register',
    '/api/pizzas',
];

const adminRoutes = [
    '/api/admin',
    '/api/users',
    '/api/delete'
];

const clientRoutes = [
    '/api/orders',
    '/api/account'
];

const compteNeededRoutes = [
    '/api/profile',
    '/api/settings'
];

function generateAccessToken(payload) {
    return jwt.sign(payload, process.env.JWT_SECRET, { expiresIn: '15m' });
}

function generateRefreshToken(payload) {
    return jwt.sign(payload, process.env.JWT_REFRESH_SECRET, { expiresIn: '7d' });
}

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
        req.user = decoded.compte;

        if (req.user) {
            req.headers['x-compte'] = JSON.stringify(req.user);
            console.log("En-tête x-compte ajouté dans le proxy :", req.headers['x-compte']);
        }

        console.log("Corps de la requête envoyé au backend :", JSON.stringify(req.body));
        console.log("URL de la requête :", req.path);
        next();
    } catch (error) {
        console.error("Erreur de vérification du JWT :", error);
        return res.status(401).json({ message: 'Jeton invalide ou expiré' });
    }
}

// Gestion du login
app.post('/api/auth/login', express.json(), async (req, res) => {
    const { username, password } = req.body;
    const auth = { username: username, password: password };
    try {
        const response = await axios.post(`${process.env.BACK}/api/authentification`, auth);
        const compte = response.data;
        if (compte != null) {
            const accessToken = generateAccessToken({ compte });
            const refreshToken = generateRefreshToken({ compte });

            res.cookie('refreshToken', refreshToken, {
                httpOnly: true,
                secure: process.env.NODE_ENV === 'production',
                sameSite: 'Strict'
            });

            return res.json({ message: 'Connexion réussie', accessToken });
        } else {
            return res.status(401).json({ message: 'Identifiants incorrects' });
        }
    } catch (error) {
        console.log(error);
        return res.status(401).json({ message: 'Problème avec le Serveur' });
    }
});

// Rafraîchissement du token
app.post('/api/auth/refresh', express.json(), (req, res) => {
    const refreshToken = req.cookies.refreshToken;

    if (!refreshToken) {
        return res.status(401).json({ message: 'Jeton de rafraîchissement manquant' });
    }

    try {
        const decoded = jwt.verify(refreshToken, process.env.JWT_REFRESH_SECRET);
        const accessToken = generateAccessToken({ username: decoded.username });

        return res.json({ accessToken: accessToken });
    } catch (error) {
        return res.status(401).json({ message: 'Jeton de rafraîchissement invalide ou expiré' });
    }
});

// Déconnexion
app.post('/api/auth/logout', (req, res) => {
    res.clearCookie('refreshToken', {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'Strict'
    });
    return res.json({ message: 'Déconnexion réussie' });
});

app.use(authMiddleware);

app.all('/*', (req, res) => {
    console.log(`[PROXY] Requête reçue : ${req.method} ${req.url}`);

    proxy.web(req, res, {
        target: process.env.BACK,
        selfHandleResponse: false
    }, (err) => {
        console.error(`[PROXY] Erreur lors de la redirection : ${err.message}`);
        res.status(500).json({ message: 'Erreur de redirection' });
    });
});

app.listen(process.env.PORT, () => {
    console.log(`Serveur proxy en écoute sur le port ${process.env.PORT}`);
});
