package com.example.pizzeria.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
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
import com.example.pizzeria.ui.ArticleViewModel
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
        setupFabListener()  // Añadido para escuchar el clic en el FAB
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this@MainActivity).get(ArticleViewModel::class.java)

        // Observar los cambios en los artículos filtrados
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

    private fun setupListeners() {
        // Listener para el campo de filtro por texto
        binding.etFilterText.doAfterTextChanged { text ->
            viewModel.currentFilterText.value = text?.toString() ?: ""
        }

        // Listener para el Spinner de filtro de tipo
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

        // Listener para el grupo de botones de ordenamiento
        binding.sortGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.currentOrder.value = when (checkedId) {
                R.id.rbSortReference -> Strings.ORDER_BY_REFERENCE
                else -> Strings.ORDER_BY_DESCRIPTION
            }
        }
    }

    private fun setupFabListener() {
        // Configurar el FloatingActionButton para abrir la actividad de creación
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            navigateToAdd()  // Llamar al método que abre la actividad de creación
        }
    }

    private fun navigateToAdd() {
        // Crear una Intent para abrir la actividad de agregar una nueva pizza
        val intent = Intent(this, AddEditPizzaActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToEdit(article: Article) {
        val intent = Intent(this, AddEditPizzaActivity::class.java).apply {
            putExtra("article", article)  // Enviar el artículo seleccionado para editar
        }
        startActivity(intent)
    }

    private fun showDeleteDialog(article: Article) {
        // Confirmación para eliminar el artículo
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_title))
            .setMessage(getString(R.string.delete_message, article.referencia))
            .setPositiveButton(getString(R.string.eliminar)) { _, _ ->
                viewModel.delete(article) // Llamar a delete en el ViewModel
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}
