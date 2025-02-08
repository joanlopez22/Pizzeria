package com.example.pizzeria.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pizzeria.R
import com.example.pizzeria.data.Article
import com.example.pizzeria.databinding.ActivityAddEditBinding

class AddEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding
    private var currentArticle: Article? = null
    private lateinit var viewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(ArticleViewModel::class.java)

        setupUI()
    }

    private fun setupUI() {
        currentArticle = intent.getParcelableExtra("article")
        currentArticle?.let { fillForm(it) }

        binding.btnSave.setOnClickListener { validateAndSave() }

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
        val referencia = binding.etReference.text.toString().trim().uppercase()
        val descripcio = binding.etDescripcio.text.toString().trim()
        val preu = binding.etPreu.text.toString().toFloatOrNull()
        val tipus = binding.spnTipus.selectedItem.toString()

        var isValid = true

        if (!isReferenciaValid(referencia, tipus)) {
            binding.tilReference.error = getString(R.string.error_referencia)
            isValid = false
        } else {
            binding.tilReference.error = null
        }

        if (descripcio.isEmpty()) {
            binding.tilDescripcio.error = getString(R.string.error_descripcio)
            isValid = false
        } else {
            binding.tilDescripcio.error = null
        }

        if (preu == null || preu <= 0) {
            binding.tilPreu.error = getString(R.string.error_preu)
            isValid = false
        } else {
            binding.tilPreu.error = null
        }

        if (isValid) {
            val article = Article(referencia, descripcio, tipus, preu!!)
            if (currentArticle == null) {
                viewModel.insert(article)
            } else {
                viewModel.update(article)
            }
            finish()
        }
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
