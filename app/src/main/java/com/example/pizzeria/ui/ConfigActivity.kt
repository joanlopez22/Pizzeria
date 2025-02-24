package com.example.pizzeria.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pizzeria.databinding.ActivityIvaBinding
class ConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIvaBinding
    private lateinit var viewModel: ArticleViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIvaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener la instancia del ViewModel
        viewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        // Cargar el valor del IVA guardado
        val savedIva = sharedPreferences.getFloat("ivaPercentage", 21f)
        binding.etIvaPercentage.setText(savedIva.toString())

        // Configurar el botón para aplicar el IVA
        binding.btnApplyIva.setOnClickListener {
            val ivaInput = binding.etIvaPercentage.text.toString()
            val iva = ivaInput.toFloatOrNull()
            if (iva != null) {
                viewModel.ivaPercentage.value = iva // Actualizar el IVA en el ViewModel

                // Guardar el valor del IVA en SharedPreferences
                with(sharedPreferences.edit()) {
                    putFloat("ivaPercentage", iva)
                    apply()
                }

                finish() // Cerrar la actividad después de aplicar
            }
        }
    }
}