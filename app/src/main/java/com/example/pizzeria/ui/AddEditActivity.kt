package com.example.pizzeria.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.pizzeria.R
import com.example.pizzeria.data.Article
import com.example.pizzeria.databinding.ActivityAddEditBinding
import com.google.android.material.snackbar.Snackbar

class AddEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding
    private var currentArticle: Article? = null
    private lateinit var articleManager: ArticleManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        articleManager = ArticleManager(application)
        setupUI()
    }

    private fun setupUI() {
        // Obtener el artículo actual si lo hay
        currentArticle = intent.getParcelableExtra("article")
        currentArticle?.let { fillForm(it) }

        binding.btnSave.setOnClickListener { validateAndSave() }

        // Configurar el spinner de tipos de pizza
        ArrayAdapter.createFromResource(
            this,
            R.array.tipus_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnTipus.adapter = adapter
        }
    }

    private fun fillForm(article: Article) {
        binding.apply {
            tilReference.editText?.setText(article.referencia)
            tilDescripcio.editText?.setText(article.descripcio)
            tilPreu.editText?.setText(article.preuSenseIva.toString())
            spnTipus.setSelection(getTypePosition(article.tipus))
        }
    }

    private fun getTypePosition(tipus: String): Int {
        return resources.getStringArray(R.array.tipus_options).indexOf(tipus)
    }

    private fun validateAndSave() {
        val referencia = binding.tilReference.editText?.text.toString().trim().uppercase()
        val descripcio = binding.tilDescripcio.editText?.text.toString().trim()
        val preu = binding.tilPreu.editText?.text.toString().toFloatOrNull()
        val tipus = binding.spnTipus.selectedItem?.toString() ?: ""

        var isValid = true
        var errorMessage = ""

        // Validaciones
        if (!isReferenciaValid(referencia, tipus)) {
            binding.tilReference.error = getString(R.string.error_referencia)
            isValid = false
            errorMessage += "Referencia incorrecta.\n"
        } else {
            binding.tilReference.error = null
        }

        if (descripcio.isEmpty()) {
            binding.tilDescripcio.error = getString(R.string.error_descripcio)
            isValid = false
            errorMessage += "Descripción vacía.\n"
        } else {
            binding.tilDescripcio.error = null
        }

        if (preu == null || preu <= 0) {
            binding.tilPreu.error = getString(R.string.error_preu)
            isValid = false
            errorMessage += "Precio inválido.\n"
        } else {
            binding.tilPreu.error = null
        }

        if (tipus.isEmpty()) {
            errorMessage += "Tipo de pizza no seleccionado.\n"
            isValid = false
        }

        if (!isValid) {
            Snackbar.make(binding.root, errorMessage.trim(), Snackbar.LENGTH_LONG).show()
            return
        }

        val article = Article(referencia, descripcio, tipus, preu!!)

        // Guardar o actualizar el artículo
        if (currentArticle == null) {
            // Si no hay un artículo actual, insertamos
            articleManager.insert(article)
        } else {
            // Si hay un artículo actual, actualizamos
            articleManager.update(article)
        }
        // Después de realizar la operación, cerramos la actividad
        finish()
    }

    private fun isReferenciaValid(referencia: String, tipus: String): Boolean {
        return when {
            referencia.length < 6 -> false
            tipus == Article.TIPUS_PIZZA && !referencia.startsWith("PI") -> false
            tipus == Article.TIPUS_VEGANA && !referencia.startsWith("PV") -> false
            tipus == Article.TIPUS_CELIACA && !referencia.startsWith("PC") -> false
            tipus == Article.TIPUS_TOPPING && !referencia.startsWith("TO") -> false
            else -> true
        }
    }
}