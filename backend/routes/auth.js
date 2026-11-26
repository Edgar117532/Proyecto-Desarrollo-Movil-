const express = require("express");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const pool = require("../config/database");

const router = express.Router();

// REGISTRO DE USUARIO
router.post("/registro", async (req, res) => {
    try {
        const { nombre, correo, password } = req.body;

        if (!nombre || !correo || !password) {
            return res.status(400).json({
                mensaje: "Todos los campos son obligatorios"
            });
        }

        if (password.length < 8) {
            return res.status(400).json({
                mensaje: "La contraseña debe tener al menos 8 caracteres"
            });
        }

        const correoNormalizado = correo.trim().toLowerCase();

        const existe = await pool.query(
            "SELECT id FROM usuarios WHERE correo = $1",
            [correoNormalizado]
        );

        if (existe.rows.length > 0) {
            return res.status(409).json({
                mensaje: "Este correo ya está registrado"
            });
        }

        const passwordHash = await bcrypt.hash(password, 10);

        const resultado = await pool.query(
            `INSERT INTO usuarios (nombre, correo, password_hash)
             VALUES ($1, $2, $3)
             RETURNING id, nombre, correo`,
            [nombre.trim(), correoNormalizado, passwordHash]
        );

        return res.status(201).json({
            mensaje: "Usuario registrado correctamente",
            token: null,
            usuario: resultado.rows[0]
        });

    } catch (error) {
        console.error("Error en registro:", error);

        return res.status(500).json({
            mensaje: "Error interno del servidor"
        });
    }
});


// INICIO DE SESIÓN
router.post("/login", async (req, res) => {
    try {
        const { correo, password } = req.body;

        if (!correo || !password) {
            return res.status(400).json({
                mensaje: "Correo y contraseña son obligatorios"
            });
        }

        const correoNormalizado = correo.trim().toLowerCase();

        const resultado = await pool.query(
            "SELECT * FROM usuarios WHERE correo = $1",
            [correoNormalizado]
        );

        if (resultado.rows.length === 0) {
            return res.status(401).json({
                mensaje: "Credenciales incorrectas"
            });
        }

        const usuario = resultado.rows[0];

        const passwordValida = await bcrypt.compare(
            password,
            usuario.password_hash
        );

        if (!passwordValida) {
            return res.status(401).json({
                mensaje: "Credenciales incorrectas"
            });
        }

        const token = jwt.sign(
            {
                id: usuario.id,
                correo: usuario.correo
            },
            process.env.JWT_SECRET,
            { expiresIn: "2h" }
        );

        return res.status(200).json({
            mensaje: "Inicio de sesión exitoso",
            token: token,
            usuario: {
                id: usuario.id,
                nombre: usuario.nombre,
                correo: usuario.correo
            }
        });

    } catch (error) {
        console.error("Error en login:", error);

        return res.status(500).json({
            mensaje: "Error interno del servidor"
        });
    }
});

module.exports = router;