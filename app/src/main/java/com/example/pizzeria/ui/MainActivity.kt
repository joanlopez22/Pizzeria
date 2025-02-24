package com.example.pizzeria.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeria.R
import com.example.pizzeria.Strings
import com.example.pizzeria.data.Article
import com.example.pizzeria.databinding.ActivityMainBinding
import com.example.pizzeria.ui.adapters.ArticleAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ArticleViewModel
    private lateinit var adapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
        setupListeners()
        setupFabButtons()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        viewModel.articles.observe(this) { articles ->
            adapter.submitList(articles)
        }
    }

    private fun setupRecyclerView() {
        adapter = ArticleAdapter(
            context = this,
            onItemClick = { navigateToEdit(it) },
            onDeleteClick = { showDeleteDialog(it) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }
    private fun showCloseAppDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Cerrar aplicación")
            .setMessage("La aplicación se cerrará en 3 segundos.")
            .setCancelable(false)
            .show()

        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                dialog.setMessage("La aplicación se cerrará en $secondsRemaining segundos.")
            }

            override fun onFinish() {
                dialog.dismiss()
                finishAffinity() // Cierra todas las actividades de la aplicación
            }
        }.start()
    }
    private fun setupListeners() {
        // Filtro de texto
        binding.etFilterText.doAfterTextChanged { text ->
            viewModel.currentFilterText.value = text?.toString() ?: ""
        }

        // Spinner de tipo
        ArrayAdapter.createFromResource(
            this,
            R.array.tipus_filtre_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerFilterType.adapter = adapter
        }

        binding.spinnerFilterType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selected = resources.getStringArray(R.array.tipus_filtre_options)[pos]
                viewModel.currentFilterType.value = when (selected) {
                    "Tots els tipus" -> "ALL"
                    else -> selected
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Ordenación
        binding.sortGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.currentOrder.value = when (checkedId) {
                R.id.rbSortReference -> Strings.ORDER_BY_REFERENCE
                else -> Strings.ORDER_BY_DESCRIPTION
            }
        }

    }

    private fun setupFabButtons() {
        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, AddEditActivity::class.java))
        }

        findViewById<FloatingActionButton>(R.id.fabSettings).setOnClickListener {
            startActivity(Intent(this, ConfigActivity::class.java))
        }
    }

    private fun navigateToEdit(article: Article) {
        Intent(this, AddEditActivity::class.java).apply {
            putExtra("article", article)
            startActivity(this)
        }
    }

    private fun showDeleteDialog(article: Article) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_title))
            .setMessage(getString(R.string.delete_message, article.referencia))
            .setPositiveButton(getString(R.string.eliminar)) { _, _ ->
                viewModel.delete(article)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}