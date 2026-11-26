
package com.example.perfilusuarioapp.network

import com.example.perfilusuarioapp.model.AuthResponse
import com.example.perfilusuarioapp.model.LoginRequest
import com.example.perfilusuarioapp.model.RegistroRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/registro")
    suspend fun registrarUsuario(
        @Body datos: RegistroRequest
    ): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun iniciarSesion(
        @Body datos: LoginRequest
    ): Response<AuthResponse>
}
