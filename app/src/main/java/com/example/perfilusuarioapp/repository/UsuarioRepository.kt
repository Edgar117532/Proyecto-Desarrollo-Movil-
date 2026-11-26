package com.example.perfilusuarioapp.repository

import com.example.perfilusuarioapp.model.LoginRequest
import com.example.perfilusuarioapp.model.RegistroRequest
import com.example.perfilusuarioapp.network.RetrofitClient

class UsuarioRepository {

    private val api = RetrofitClient.api

    suspend fun registrar(
        nombre: String,
        correo: String,
        password: String
    ) = api.registrarUsuario(
        RegistroRequest(nombre, correo, password)
    )

    suspend fun login(
        correo: String,
        password: String
    ) = api.iniciarSesion(
        LoginRequest(correo, password)
    )
}
