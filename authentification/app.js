const express = require('express');
const httpProxy = require('http-proxy');
const jwt = require('jsonwebtoken');
const cookieParser = require('cookie-parser');
const proxy = httpProxy.createProxyServer();
const app = express();
require('dotenv').config();
const axios = require('axios');
const cors = require('cors');



app.use(cors({
    origin: process.env.FRONT,
    credentials: true,
}));

app.use(express.json());
app.use(cookieParser());

// Routes publiques sans authentification
const publicRoutes = [
    '/api/pizzas',
    '/api/auth/login',
    '/api/auth/register',
    '/api/auth/refresh'
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
        req.user = jwt.verify(token, process.env.JWT_SECRET);
        next();
    } catch (error) {
        return res.status(401).json({ message: 'Jeton invalide ou expiré' });
    }
}

app.post('/api/auth/login', async (req, res) => {
    const { username, password } = req.body;
    const auth = { username: username, password: password };
    try {
        const response = await axios.post(`${process.env.BACK}/api/authentification`, auth);
        if (response.data) {
            const accessToken = generateAccessToken({ username });
            const refreshToken = generateRefreshToken({ username });

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

app.post('/api/auth/refresh', (req, res) => {
    const refreshToken = req.cookies.refreshToken;

    if (!refreshToken) {
        return res.status(401).json({ message: 'Jeton de rafraîchissement manquant' });
    }

    try {
        const decoded = jwt.verify(refreshToken, process.env.JWT_REFRESH_SECRET);
        const accessToken = generateAccessToken({ username: decoded.username });

        return res.json({ accessToken : accessToken });
    } catch (error) {
        return res.status(401).json({ message: 'Jeton de rafraîchissement invalide ou expiré' });
    }
});

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
    proxy.web(req, res, { target: process.env.BACK });
});

app.listen(process.env.PORT, () => {
    console.log(`Serveur proxy en écoute sur le port ${process.env.PORT}`);
});
