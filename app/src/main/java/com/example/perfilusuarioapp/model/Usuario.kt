package com.example.perfilusuarioapp.model

data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String
)

data class RegistroRequest(
    val nombre: String,
    val correo: String,
    val password: String
)

data class LoginRequest(
    val correo: String,
    val password: String
)

data class AuthResponse(
    val mensaje: String,
    val token: String?,
    val usuario: Usuario?
)
