package com.example.pizzeria.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeria.R
import com.example.pizzeria.Strings
import com.example.pizzeria.data.Article
import com.example.pizzeria.databinding.ActivityMainBinding
import com.example.pizzeria.ui.adapters.ArticleAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    // Declaración de variables lateinit para binding, manager y adapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var articleManager: ArticleManager
    private lateinit var adapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar el layout usando View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el RecyclerView y el adaptador primero
        setupRecyclerView()
        // Configurar el ArticleManager y cargar datos
        setupArticleManager()
        // Configurar los listeners para filtros y ordenamiento
        setupListeners()
        // Configurar el botón FAB para agregar artículos
        setupFabListener()
        // Configurar el botón FAB para ir a la configuración
        setupConfigButton()
    }

    /** Configura el ArticleManager y carga la lista inicial de artículos */
    private fun setupArticleManager() {
        articleManager = ArticleManager(application)
        updateArticleList() // Actualiza la lista al iniciar
    }

    /** Configura el RecyclerView y su adaptador */
    private fun setupRecyclerView() {
        adapter = ArticleAdapter(
            context = this,
            onItemClick = { navigateToEdit(it) }, // Navegar a edición al hacer clic en un artículo
            onDeleteClick = { showDeleteDialog(it) } // Mostrar diálogo de eliminación
        )
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    /** Configura los listeners para filtros y ordenamiento */
    private fun setupListeners() {
        // Listener para el campo de texto de filtro
        binding.etFilterText.doAfterTextChanged { text ->
            articleManager.currentFilterText = text?.toString() ?: ""
            updateArticleList() // Actualizar la lista al cambiar el texto
        }

        // Configurar el Spinner para el filtro de tipo
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.tipus_filtre_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilterType.adapter = adapter

        // Listener para el Spinner de filtro de tipo
        binding.spinnerFilterType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selected = resources.getStringArray(R.array.tipus_filtre_options)[pos]
                articleManager.currentFilterType = when (selected) {
                    "Tots els tipus" -> "ALL"
                    else -> selected
                }
                updateArticleList() // Actualizar la lista al cambiar el tipo
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Listener para el grupo de botones de ordenamiento
        binding.sortGroup.setOnCheckedChangeListener { _, checkedId ->
            articleManager.currentOrder = when (checkedId) {
                R.id.rbSortReference -> Strings.ORDER_BY_REFERENCE
                else -> Strings.ORDER_BY_DESCRIPTION
            }
            updateArticleList() // Actualizar la lista al cambiar el orden
        }
    }

    /** Configura el botón FAB para agregar nuevos artículos */
    private fun setupFabListener() {
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            navigateToAdd()
        }
    }

    /** Navega a la actividad para agregar un artículo */
    private fun navigateToAdd() {
        val intent = Intent(this, AddEditActivity::class.java)
        startActivity(intent)
    }

    /** Navega a la actividad para editar un artículo */
    private fun navigateToEdit(article: Article) {
        val intent = Intent(this, AddEditActivity::class.java).apply {
            putExtra("article", article)
        }
        startActivity(intent)
    }

    /** Configura el botón FAB para ir a la pantalla de configuración */
    private fun setupConfigButton() {
        val fabSettings: FloatingActionButton = findViewById(R.id.fabSettings)
        fabSettings.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }
    }

    /** Muestra un diálogo de confirmación para eliminar un artículo */
    private fun showDeleteDialog(article: Article) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_title))
            .setMessage(getString(R.string.delete_message, article.referencia))
            .setPositiveButton(getString(R.string.eliminar)) { _, _ ->
                articleManager.delete(article) // Eliminar el artículo
                updateArticleList() // Actualizar la lista tras eliminar
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    /** Actualiza la lista de artículos en el adaptador */
    private fun updateArticleList() {
        val articles = articleManager.getFilteredArticles()
        adapter.submitList(articles) // Enviar la lista actualizada al adaptador
    }

    /** Actualiza la lista al volver a la actividad */
    override fun onResume() {
        super.onResume()
        updateArticleList()
    }
}