
package com.example.perfilusuarioapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.perfilusuarioapp.R
import com.example.perfilusuarioapp.repository.UsuarioRepository
import kotlinx.coroutines.launch
import com.example.perfilusuarioapp.ui.profile.ProfileActivity
import com.example.perfilusuarioapp.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private val repository = UsuarioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val correo = findViewById<EditText>(R.id.etCorreo)
        val password = findViewById<EditText>(R.id.etPassword)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegistro = findViewById<Button>(R.id.btnIrRegistro)

        btnLogin.setOnClickListener {

            val email = correo.text.toString().trim()
            val clave = password.text.toString()

            if (email.isEmpty() || clave.isEmpty()) {
                Toast.makeText(
                    this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val respuesta = repository.login(email, clave)

                    if (respuesta.isSuccessful) {

                        val resultado = respuesta.body()

                        if (resultado?.usuario != null &&
                            !resultado.token.isNullOrEmpty()
                        ) {
                            val intent = Intent(
                                this@LoginActivity,
                                ProfileActivity::class.java
                            )

                            intent.putExtra(
                                "nombre",
                                resultado.usuario.nombre
                            )

                            intent.putExtra(
                                "correo",
                                resultado.usuario.correo
                            )

                            intent.putExtra(
                                "token",
                                resultado.token
                            )

                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Respuesta incompleta del servidor",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Credenciales incorrectas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        btnRegistro.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }
    }
}
