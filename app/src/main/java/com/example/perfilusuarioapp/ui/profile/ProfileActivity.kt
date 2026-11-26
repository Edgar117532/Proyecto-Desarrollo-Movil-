package com.example.perfilusuarioapp.ui.profile

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.perfilusuarioapp.R
import com.example.perfilusuarioapp.ui.login.LoginActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var imgPerfil: ImageView

    // Abrir la cámara y recibir una fotografía
    private val tomarFoto = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->

        if (bitmap != null) {
            imgPerfil.setImageBitmap(bitmap)

            Toast.makeText(
                this,
                "Fotografía actualizada",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Abrir la galería y seleccionar una imagen
    private val seleccionarImagen = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        if (uri != null) {
            imgPerfil.setImageURI(uri)

            Toast.makeText(
                this,
                "Imagen de perfil actualizada",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Referencias de los elementos XML
        imgPerfil = findViewById(R.id.imgPerfil)

        val tvNombre = findViewById<TextView>(
            R.id.tvNombrePerfil
        )

        val tvCorreo = findViewById<TextView>(
            R.id.tvCorreoPerfil
        )

        val btnCamara = findViewById<Button>(
            R.id.btnTomarFoto
        )

        val btnGaleria = findViewById<Button>(
            R.id.btnGaleria
        )

        val btnCerrarSesion = findViewById<Button>(
            R.id.btnCerrarSesion
        )

        // Recibir información del usuario
        val nombre = intent.getStringExtra("nombre") ?: "Usuario"
        val correo = intent.getStringExtra("correo") ?: ""

        tvNombre.text = nombre
        tvCorreo.text = correo

        // Acción para tomar fotografía
        btnCamara.setOnClickListener {
            tomarFoto.launch(null)
        }

        // Acción para seleccionar imagen
        btnGaleria.setOnClickListener {
            seleccionarImagen.launch("image/*")
        }

        // Cerrar sesión
        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }
}
