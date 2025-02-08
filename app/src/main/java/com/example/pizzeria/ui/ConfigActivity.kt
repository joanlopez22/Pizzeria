package com.example.pizzeria.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.pizzeria.databinding.ActivityAddEditBinding // Asegúrate de que este nombre esté correcto

class ConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        setupUI()
    }

    private fun setupUI() {
        // Cargar el IVA guardado (si existe) o asignar un valor por defecto (0.10f).
        binding.etPreu.setText(sharedPrefs.getFloat("iva", 0.10f).toString())
        binding.btnSave.setOnClickListener { saveSettings() }
    }

    private fun saveSettings() {
        // Obtener el precio sin IVA desde el EditText.
        val precioSinIva = binding.etPreu.text.toString().toFloatOrNull() ?: 0f
        // Obtener el IVA guardado (o un valor por defecto).
        val iva = sharedPrefs.getFloat("iva", 0.10f)

        // Aplicar el IVA al precio sin IVA.
        val precioConIva = precioSinIva * (1 + iva)

        // Guardar el precio con IVA (si lo deseas guardar en las preferencias).
        sharedPrefs.edit().putFloat("precioConIva", precioConIva).apply()

        // Aquí puedes actualizar el RecyclerView para que muestre el precio con IVA.

        finish()  // Finalizar la actividad y volver atrás.
    }
}
