// ui/MainActivity.kt
package com.example.pizzeria.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzeria.R
import com.example.pizzeria.data.Article
import com.example.pizzeria.databinding.ActivityMainBinding

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
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[ArticleViewModel::class.java]
        viewModel.articles.observe(this) { adapter.submitList(it) }
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

    // En MainActivity.kt (dentro de setupListeners())
    private fun setupListeners() {
        // Filtro por texto
        binding.etFilterText.doAfterTextChanged { text ->
            viewModel.currentFilterText.value = text?.toString() ?: ""
        }

        // Filtro por tipo
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

    // En MainActivity.kt (completar navigateToAdd() y navigateToEdit())
    private fun navigateToAdd() {
        val intent = Intent(this, AddEditActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToEdit(article: Article) {
        val intent = Intent(this, AddEditActivity::class.java).apply {
            putExtra("article", article)
        }
        startActivity(intent)
    }
    private fun showDeleteDialog(article: Article) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_title))
            .setMessage(getString(R.string.delete_message, article.referencia))
            .setPositiveButton(getString(R.string.delete)) { _, _ -> viewModel.delete(article) }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

}