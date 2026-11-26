package com.example.perfilusuarioapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.perfilusuarioapp.R
import com.example.perfilusuarioapp.repository.UsuarioRepository
import com.example.perfilusuarioapp.ui.login.LoginActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val repository = UsuarioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nombre = findViewById<EditText>(R.id.etNombre)
        val correo = findViewById<EditText>(R.id.etCorreoRegistro)
        val password = findViewById<EditText>(R.id.etPasswordRegistro)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {

            val name = nombre.text.toString().trim()
            val email = correo.text.toString().trim()
            val clave = password.text.toString()

            if (name.isEmpty() || email.isEmpty() || clave.isEmpty()) {
                Toast.makeText(
                    this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(
                    this,
                    "Ingresa un correo válido",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (clave.length < 8) {
                Toast.makeText(
                    this,
                    "La contraseña debe tener al menos 8 caracteres",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val respuesta = repository.registrar(name, email, clave)

                    if (respuesta.isSuccessful) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registro exitoso. Ahora inicia sesión.",
                            Toast.LENGTH_LONG
                        ).show()

                        startActivity(
                            Intent(
                                this@RegisterActivity,
                                LoginActivity::class.java
                            )
                        )
                        finish()

                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "No se pudo registrar. Código: ${respuesta.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
