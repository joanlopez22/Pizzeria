package com.example.pizzeria.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pizzeria.databinding.ActivityIvaBinding
class ConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIvaBinding
    private lateinit var viewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIvaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener la instancia del ViewModel
        viewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        // Configurar el botón para aplicar el IVA
        binding.btnApplyIva.setOnClickListener {
            val ivaInput = binding.etIvaPercentage.text.toString()
            val iva = ivaInput.toFloatOrNull()
            if (iva != null) {
                viewModel.ivaPercentage.value = iva // Actualizar el IVA
                finish() // Cerrar la actividad después de aplicar
            }
        }
    }
}
