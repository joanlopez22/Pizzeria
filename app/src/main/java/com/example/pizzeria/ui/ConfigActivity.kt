package com.example.pizzeria.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.pizzeria.databinding.ActivityMainBinding  // Cambia esto a ActivityMainBinding

class ConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding  // Usa ActivityMainBinding
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)  // Cambia esto a ActivityMainBinding
        setContentView(binding.root)

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        setupUI()
    }

    private fun setupUI() {
        binding.etIva.setText(sharedPrefs.getFloat("iva", 0.10f).toString())
        binding.btnSave.setOnClickListener { saveSettings() }
    }

    private fun saveSettings() {
        val iva = binding.etIva.text.toString().toFloatOrNull() ?: 0.10f
        sharedPrefs.edit().putFloat("iva", iva).apply()
        finish()
    }
}
