require("dotenv").config();

const express = require("express");
const cors = require("cors");
const pool = require("./config/database");
const authRoutes = require("./routes/auth");

const app = express();

app.use(cors());
app.use(express.json());

app.get("/api/health", async (req, res) => {
    try {
        await pool.query("SELECT NOW()");

        res.json({
            mensaje: "Backend funcionando correctamente",
            baseDatos: "PostgreSQL conectado"
        });
    } catch (error) {
        res.status(500).json({
            mensaje: "Error al conectar con PostgreSQL"
        });
    }
});

app.use("/api/auth", authRoutes);

const PORT = process.env.PORT || 3000;

app.listen(PORT, "0.0.0.0", () => {
    console.log(`Servidor iniciado en el puerto ${PORT}`);
});