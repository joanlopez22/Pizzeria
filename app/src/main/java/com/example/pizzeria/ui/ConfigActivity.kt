package com.example.pizzeria.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pizzeria.databinding.ActivityIvaBinding
import android.content.Context

class ConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIvaBinding
    private lateinit var articleManager: ArticleManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIvaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar ArticleManager
        articleManager = ArticleManager(application)

        // Mostrar el IVA actual en el campo de texto
        binding.etIvaPercentage.setText(articleManager.ivaPercentage.toString())

        // Configurar el botón para aplicar el IVA
        binding.btnApplyIva.setOnClickListener {
            val ivaInput = binding.etIvaPercentage.text.toString()
            val iva = ivaInput.toFloatOrNull()
            if (iva != null && iva >= 0) {
                // Actualizar el IVA en ArticleManager
                articleManager.ivaPercentage = iva

                // Guardar el IVA en SharedPreferences para que persista
                val prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
                prefs.edit().putFloat("ivaPercentage", iva).apply()

                finish() // Cerrar la actividad después de aplicar
            } else {
                binding.etIvaPercentage.error = "Porcentaje de IVA inválido"
            }
        }
    }
}